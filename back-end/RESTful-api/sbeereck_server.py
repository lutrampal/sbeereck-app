from flask import Flask
from flask_restful import Resource, Api, reqparse
from parties import Parties
from party import Party
from products import Products

app = Flask(__name__)
api = Api(app)
parser = reqparse.RequestParser()

api.add_resource(Parties, '/parties')
api.add_resource(Products, '/products')
api.add_resource(Party, '/parties/<party_id>')

if __name__ == '__main__':
    app.run(debug=True, port=8081, host='0.0.0.0')
