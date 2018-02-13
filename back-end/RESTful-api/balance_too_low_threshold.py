from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class BalanceTooLowThreshold(Resource):
    def get(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "SELECT balance_too_low_threshold FROM parameters WHERE parameters_id = 1"
        with connection.cursor() as cursor:
            cursor.execute(query)
            if cursor.rowcount < 1:
                connection.close()
                abort(404)
            row = cursor.fetchone()
        connection.close()
        return row, 201

    def put(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        if request.json is None:
            abort(400, description="request should be json")
        if 'balance_too_low_threshold' not in request.json:
            abort(400, description="missing balance_too_low_threshold key in JSON body")
        update_threshold_query = "UPDATE parameters " \
                                 "SET balance_too_low_threshold = %(balance_too_low_threshold)s " \
                                 "WHERE parameters_id = 1"
        with connection.cursor() as cursor:
            cursor.execute(update_threshold_query, request.json)
        connection.commit()
        connection.close()
        return 201
