from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request
from datetime import datetime
import datetime


class Membership(Resource):

    def put(self, member_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        update_query = "UPDATE members SET last_membership_payment = %(date)s WHERE member_id = %(id)s"
        args = {
            "date": datetime.date.today().strftime("%Y-%m-%d"),
            "id": member_id
        }
        with connection.cursor() as cursor:
            if cursor.execute(update_query, args) < 1:
                connection.close()
                abort(404)
        connection.commit()
        connection.close()
        return 201
