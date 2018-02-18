from passlib.hash import pbkdf2_sha256
from flask_restful import reqparse
import pymysql

DB_HOST = '172.17.0.2'
DB_PORT = 3306
DB_NAME = 'sbeereck-db'
DB_USER = 'root'
DB_PASSWORD = 'toor'


# Tokens are made of a 4 digits prefix (the id) and a password
def is_token_valid(token, dbconnection):
    token_id = token[:4]
    token_pass = token[4:]
    query = "SELECT hashed_token FROM authentication_tokens WHERE token_id=%s"
    with dbconnection.cursor() as cursor:
        cursor.execute(query, token_id)
        res = cursor.fetchone()
        if res is None:
            return False
        return pbkdf2_sha256.verify(token_pass, res['hashed_token'])


def get_db_connection():
    return pymysql.connect(user=DB_USER,
                           password=DB_PASSWORD,
                           host=DB_HOST,
                           port=DB_PORT,
                           db=DB_NAME,
                           charset='utf8mb4',
                           cursorclass=pymysql.cursors.DictCursor)


def get_default_parser():
    parser = reqparse.RequestParser()
    parser.add_argument('authentication-token', required=True, location='headers')
    return parser
