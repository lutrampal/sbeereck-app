from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class FullMembers(Resource):
    def get(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "SELECT member_id AS id, first_name, last_name, school, email, phone, balance, last_membership_payment, " \
                "is_former_staff " \
                "FROM members " \
                "WHERE is_deleted=0 " \
                "ORDER BY is_former_staff, last_name, first_name"
        members = []
        with connection.cursor() as cursor:
            cursor.execute(query)
            for row in cursor.fetchall():
                row['last_membership_payment'] = str(row['last_membership_payment'])
                if row['is_former_staff'] == b'\x01':
                    row['is_former_staff'] = True
                else:
                    row['is_former_staff'] = False
                members.append(row)
        connection.close()
        return members, 200
