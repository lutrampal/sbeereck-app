from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Products(Resource):
    def get(self):
        parser = get_default_parser()
        parser.add_argument('type', required=False, type=str)
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "SELECT product_id, name, price, type FROM products"
        if args['type'] is not None:
            query += " WHERE type = %(type)s"
        query += " ORDER BY name ASC"
        products = []
        with connection.cursor() as cursor:
            cursor.execute(query, args)
            for row in cursor.fetchall():
                products.append({
                    'id': row['product_id'],
                    'name': row['name'],
                    'price': row['price'],
                    'type': row['type']
                })
        connection.close()
        return products, 201
