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
        query += " ORDER BY name ASC, type ASC"
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

    def post(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        if request.json is None:
            abort(400, description="request should be json")
        for key in ['name', 'price', 'type']:
            if key not in request.json:
                abort(400, description="missing " + key + " key in JSON body")
        insert_product_query = "INSERT INTO products(name, price, type) " \
                               "VALUES(%(name)s, %(price)s, %(type)s)"
        with connection.cursor() as cursor:
            cursor.execute(insert_product_query, request.json)
            product_id = connection.insert_id()
        connection.commit()
        connection.close()
        return {'id': product_id}, 201
