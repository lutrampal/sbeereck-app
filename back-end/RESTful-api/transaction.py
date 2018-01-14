from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Transaction(Resource):
    def delete(self, transaction_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        delete_query = "DELETE FROM transactions WHERE transaction_id = %s"
        with connection.cursor() as cursor:
            if cursor.execute(delete_query, transaction_id) < 1:
                connection.close()
                abort(404)
        connection.commit()
        connection.close()
        return 201
