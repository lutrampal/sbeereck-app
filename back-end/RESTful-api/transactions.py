from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Transactions(Resource):

    def post(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        if request.json is None:
            abort(400, description="request should be json")
        for key in ['member_id', 'amount', 'label']:
            if key not in request.json:
                abort(400, description="missing " + key + " key in JSON body")
        if 'party_id' not in request.json:
            request.json['party_id'] = None
        insert_transaction_query = "INSERT INTO transactions(member_id, party_id, label, amount) " \
                                   "VALUES(%(member_id)s, %(party_id)s, %(label)s, %(amount)s)"
        with connection.cursor() as cursor:
            cursor.execute(insert_transaction_query, request.json)
            transaction_id = connection.insert_id()
        connection.commit()
        connection.close()
        return {'id': transaction_id}, 201


