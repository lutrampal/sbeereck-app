from flask import Flask
from flask_restful import Resource, Api, reqparse
from parties import Parties


app = Flask(__name__)
api = Api(app)
parser = reqparse.RequestParser()

api.add_resource(Parties, '/parties')

if __name__ == '__main__':
    app.run(debug=True, port=8081, host='0.0.0.0')
