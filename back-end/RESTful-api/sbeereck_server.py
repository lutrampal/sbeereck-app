from flask import Flask
from flask_restful import Resource, Api, reqparse
from parties import Parties
from party import Party
from products import Products
from product import Product
from members import Members
from member import Member
from membership import Membership

app = Flask(__name__)
api = Api(app)
parser = reqparse.RequestParser()

api.add_resource(Parties, '/parties')
api.add_resource(Products, '/products')
api.add_resource(Members, '/members')
api.add_resource(Party, '/parties/<party_id>')
api.add_resource(Product, '/products/<product_id>')
api.add_resource(Member, '/members/<member_id>')
api.add_resource(Membership, '/members/<member_id>/membership')

if __name__ == '__main__':
    app.run(debug=True, port=8081, host='0.0.0.0')
