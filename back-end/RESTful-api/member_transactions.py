from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class MemberTransactions(Resource):
    def get(self, member_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "SELECT t.party_id AS party_id, name AS party_name, amount, label, timestamp " \
                "FROM transactions t INNER JOIN parties p ON t.party_id=p.party_id " \
                "WHERE member_id=%s " \
                "ORDER BY timestamp DESC"
        transactions = []
        with connection.cursor() as cursor:
            cursor.execute(query, member_id)
            for row in cursor.fetchall():
                row['timestamp'] = str(row['timestamp'])
                transactions.append(row)
        connection.close()
        return transactions, 200



