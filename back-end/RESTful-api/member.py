from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Member(Resource):

    def get(self, member_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        select_member_query = "SELECT first_name, last_name, balance, last_membership_payment, school, email, phone, " \
                              "is_former_staff " \
                              "FROM members " \
                              "WHERE member_id = %(id)s"
        args = {
            "id": member_id
        }
        with connection.cursor() as cursor:
            cursor.execute(select_member_query, args)
            if cursor.rowcount < 1:
                connection.close()
                abort(404)
            row = cursor.fetchone()
            row['last_membership_payment'] = str(row['last_membership_payment'])
            if row['is_former_staff'] == b'\x01':
                row['is_former_staff'] = True
            else:
                row['is_former_staff'] = False
            row['id'] = member_id
        connection.close()
        return row, 201

    def delete(self, member_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        update_query = "UPDATE members SET is_deleted = 1 WHERE member_id = %s"
        with connection.cursor() as cursor:
            if cursor.execute(update_query, member_id) < 1:
                connection.close()
                abort(404)
        connection.commit()
        connection.close()
        return 201