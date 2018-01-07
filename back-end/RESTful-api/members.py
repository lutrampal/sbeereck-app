from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser
from flask import request


class Members(Resource):
    def get(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        query = "SELECT member_id AS id, first_name, last_name, balance, last_membership_payment, is_former_staff " \
                "FROM member_list"
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
        return members, 201

    def post(self):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        for key in ['first_name', 'last_name', 'school', 'phone', 'email']:
            if key not in request.json:
                abort(400, description="missing " + key + " key in JSON body")
        insert_member_query = "INSERT INTO members(first_name, last_name, school, phone, email) " \
                              "VALUES(%(first_name)s, %(last_name)s, %(school)s, %(phone)s, %(email)s)"
        with connection.cursor() as cursor:
            cursor.execute(insert_member_query, request.json)
            member_id = connection.insert_id()
        connection.commit()
        connection.close()
        return {'id': member_id}, 201
