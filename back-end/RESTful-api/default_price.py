from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class DefaultPrice(Resource):
    def get(self, product):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        if product not in ["normal_beer", "special_beer"]:
            connection.close()
            abort(404)
        query = "SELECT default_" + product + "_price AS default_product_price FROM parameters WHERE parameters_id = 1"
        with connection.cursor() as cursor:
            cursor.execute(query)
            if cursor.rowcount < 1:
                connection.close()
                abort(404)
            row = cursor.fetchone()
        connection.close()
        return row, 200

    def put(self, product):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        if request.json is None:
            abort(400, description="request should be json")
        if 'default_product_price' not in request.json:
            abort(400, description="missing default_product_price key in JSON body")
        if product not in ["normal_beer", "special_beer"]:
            connection.close()
            abort(404)
        update_price_query = "UPDATE parameters " \
                             "SET default_" + product + "_price = %(default_product_price)s " \
                             "WHERE parameters_id = 1"
        with connection.cursor() as cursor:
            request.json['product'] = product
            cursor.execute(update_price_query, request.json)
        connection.commit()
        connection.close()
        return 200
