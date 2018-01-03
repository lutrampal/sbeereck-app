from flask_restful import Resource, abort
from utilities import get_db_connection, is_token_valid, get_default_parser


class Party(Resource):
    def delete(self, party_id):
        parser = get_default_parser()
        args = parser.parse_args()
        connection = get_db_connection()
        if not is_token_valid(args['authentication-token'], connection):
            connection.close()
            abort(403)
        update_query = "UPDATE parties SET is_deleted = 1 WHERE party_id = %s"
        select_query = "SELECT party_id, name, date, number_of_attendees, balance " \
                       "FROM party_list WHERE party_id = %s"
        with connection.cursor() as cursor:
            cursor.execute(select_query, party_id)
            if cursor.rowcount < 1:
                connection.rollback()
                connection.close()
                abort(404)
            row = cursor.fetchone()
            party = {
                'id': row['party_id'],
                'name': row['name'],
                'date': str(row['date']),
                'number_of_attendees': row['number_of_attendees'],
                'balance': row['balance']
            }
            cursor.execute(update_query, party_id)
        connection.commit()
        connection.close()
        return party, 201
