from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Party(Resource):

    def get(self, party_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        select_party_query = "SELECT name, date, normal_beer_price, special_beer_price " \
                             "FROM parties " \
                             "WHERE party_id = %(id)s"
        select_served_beers_query = "SELECT s.product_id AS id, name, category, price " \
                                    "FROM served_beers_at_party s " \
                                    "INNER JOIN products p ON s.product_id = p.product_id " \
                                    "WHERE s.party_id = %(id)s " \
                                    "ORDER BY name ASC, category ASC"
        party = {
            "id": party_id
        }
        with connection.cursor() as cursor:
            cursor.execute(select_party_query, party)
            if cursor.rowcount < 1:
                connection.close()
                abort(404)
            row = cursor.fetchone()
            party['date'] = str(row['date'])
            party['name'] = row['name']
            party['normal_beer_price'] = row['normal_beer_price']
            party['special_beer_price'] = row['special_beer_price']
            party['served_beers'] = []
            cursor.execute(select_served_beers_query, party)
            for row in cursor.fetchall():
                party['served_beers'].append(row)
        connection.close()
        return party, 201

    def delete(self, party_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        update_query = "UPDATE parties SET is_deleted = 1 WHERE party_id = %s"
        with connection.cursor() as cursor:
            if cursor.execute(update_query, party_id) < 1:
                connection.close()
                abort(404)
        connection.commit()
        connection.close()
        return 201

    def put(self, party_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        if request.json is None:
            abort(400, description="request should be json")
        for key in ['name', 'date', 'normal_beer_price', 'special_beer_price', 'served_beers']:
            if key not in request.json:
                abort(400, description="missing " + key + " key in JSON body")
        check_party_exist_query = "SELECT party_id FROM parties WHERE party_id = %(id)s"
        update_party_query = "UPDATE parties " \
                             "SET name = %(name)s, date = %(date)s, normal_beer_price = %(normal_beer_price)s, " \
                             "special_beer_price = %(special_beer_price)s " \
                             "WHERE party_id = %(id)s"
        delete_beer_query = "DELETE FROM served_beers_at_party WHERE party_id = %(id)s"
        insert_beer_query = "INSERT INTO served_beers_at_party(party_id, product_id, category) " \
                            "VALUES(%(party_id)s, %(product_id)s, %(category)s)"
        party = {
            'id': party_id,
            'name': request.json['name'],
            'date': request.json['date'],
            'normal_beer_price': request.json['normal_beer_price'],
            'special_beer_price': request.json['special_beer_price']
        }
        with connection.cursor() as cursor:
            if not cursor.execute(check_party_exist_query, party):
                connection.close()
                abort(404)
            cursor.execute(update_party_query, party)
            cursor.execute(delete_beer_query, party)
            for beer in request.json['served_beers']:
                for key in ['id', 'category']:
                    if key not in beer:
                        connection.rollback()
                        connection.close()
                        abort(400, description="missing " + key + " key in served_beers item in JSON body")
                row = {
                    'party_id': party_id,
                    'product_id': beer['id'],
                    'category': beer['category']
                }
                cursor.execute(insert_beer_query, row)
        connection.commit()
        connection.close()
        return 201
