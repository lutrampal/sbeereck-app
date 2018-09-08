from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Parties(Resource):
    def get(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "SELECT party_id, name, date, number_of_attendees, revenue, topup, balance " \
                "FROM party_list"
        parties = []
        with connection.cursor() as cursor:
            cursor.execute(query)
            for row in cursor.fetchall():
                number_of_attendees = row['number_of_attendees']
                if number_of_attendees is None:
                    number_of_attendees = 0
                revenue = row['revenue']
                if revenue is None:
                    revenue = 0
                topup = row['topup']
                if topup is None:
                    topup = 0
                balance = row['balance']
                if balance is None:
                    balance = 0
                parties.append({
                    'id': row['party_id'],
                    'name': row['name'],
                    'date': str(row['date']),
                    'number_of_attendees': number_of_attendees,
                    'revenue': revenue,
                    'topup': topup,
                    'balance': balance
                })
        connection.close()
        return parties, 201

    def post(self):
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
        insert_party_query = "INSERT INTO parties(name, date, normal_beer_price, special_beer_price) " \
                             "VALUES(%(name)s, %(date)s, %(normal_beer_price)s, %(special_beer_price)s)"
        insert_beer_query = "INSERT INTO served_beers_at_party(party_id, product_id, category) " \
                            "VALUES(%(party_id)s, %(product_id)s, %(category)s)"
        with connection.cursor() as cursor:
            party = {
                "name": request.json["name"],
                "date": request.json["date"],
                "normal_beer_price": request.json["normal_beer_price"],
                "special_beer_price": request.json["special_beer_price"]
            }
            cursor.execute(insert_party_query, party)
            party_id = connection.insert_id()
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
        return {'id': party_id}, 201


