from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask_restful import reqparse


class Parties(Resource):
    def get(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication_token'], connection):
            abort(403)
        query = "SELECT p.party_id, name, date, number_of_attendees, balance " \
                "FROM party_list"
        parties = []
        with connection.cursor() as cursor:
            cursor.execute(query)
            for row in cursor.fetchall():
                parties.append({
                    'id': row['party_id'],
                    'name': row['name'],
                    'date': row['date'],
                    'number_of_attendees': row['number_of_attendees'],
                    'balance': row['balance']
                })
        connection.close()
        return parties, 201


