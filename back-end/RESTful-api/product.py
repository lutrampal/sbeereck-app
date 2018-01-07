from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Product(Resource):
    def delete(self, product_id):
        parser = get_default_parser()
        parser.add_argument('type', required=False, type=str)
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "DELETE FROM products WHERE product_id = %(id)s"
        args = {'id': product_id}
        with connection.cursor() as cursor:
            if cursor.execute(query, args) < 1:
                connection.close()
                abort(404)
        connection.commit()
        connection.close()
        return 201

    def put(self, product_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        for key in ['name', 'price', 'type']:
            if key not in request.json:
                abort(400, description="missing " + key + " key in JSON body")
        check_product_exist_query = "SELECT product_id FROM products WHERE product_id = %(id)s"
        update_product_query = "UPDATE products " \
                               "SET name = %(name)s, price = %(price)s, type = %(type)s " \
                               "WHERE product_id = %(id)s"
        request.json['id'] = product_id
        with connection.cursor() as cursor:
            if not cursor.execute(check_product_exist_query, request.json):
                connection.close()
                abort(404)
            cursor.execute(update_product_query, request.json)
        connection.commit()
        connection.close()
        return 201
