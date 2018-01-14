from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class PartyTransactions(Resource):
    def get(self, party_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "SELECT transaction_id AS id, t.member_id AS member_id, first_name, last_name, amount, label, " \
                "timestamp " \
                "FROM transactions t INNER JOIN members m ON t.member_id = m.member_id " \
                "WHERE party_id = %s " \
                "ORDER BY timestamp DESC"
        transactions = []
        with connection.cursor() as cursor:
            cursor.execute(query, party_id)
            for row in cursor.fetchall():
                row['timestamp'] = str(row['timestamp'])
                transactions.append(row)
        connection.close()
        return transactions, 201



