# coding: utf8

# This script should be run on a brand new DB because it relies on predicted auto-incremented key values.

import sys
from datetime import timedelta
from datetime import datetime
from datetime import date
import random
import time
import pymysql

USER = 'root'
PASSWORD = 'toor'
HOST = 'localhost'
PORT = 3306
DATABASE = 'sbeereck-db'

MAX_MEMBER_ID = 633
NUMBER_OF_PARTIES = 20
NUMBER_OF_BEERS = 20
NUMBER_OF_TRANSACTIONS_PER_PARTY = 200
NUMBER_OF_SERVED_BEERS_PER_PARTY = 10
NORMAL_BEER_PRICE = 1.35
SPECIAL_BEER_PRICE = 1.70
BALANCE_TOO_LOW = -10
MEMBERSHIP_COST = 10
MEMBERSHIP_EXPIRING = date(2018, 9, 5)

CONNECTION = pymysql.connect(user=USER,
                             password=PASSWORD,
                             host=HOST,
                             port=PORT,
                             db=DATABASE,
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)


def generate_parameters():
    query = ("INSERT INTO parameters(balance_too_low_threshold, membership_cost, membership_expiring_date, "
             "default_normal_beer_price, default_special_beer_price) "
             "VALUES(%(balance_too_low_threshold)s, %(membership_cost)s, %(membership_expiring_date)s, "
             "%(default_normal_beer_price)s, %(default_special_beer_price)s)")
    params = {
        'balance_too_low_threshold': BALANCE_TOO_LOW,
        'membership_cost': MEMBERSHIP_COST,
        'membership_expiring_date': MEMBERSHIP_EXPIRING,
        'default_normal_beer_price': NORMAL_BEER_PRICE,
        'default_special_beer_price': SPECIAL_BEER_PRICE
    }
    with CONNECTION.cursor() as cursor:
        cursor.execute(query, params)
        CONNECTION.commit()


def generate_parties():
    query = ("INSERT INTO parties(name, date, normal_beer_price, special_beer_price) "
             "VALUES(%(name)s, %(date)s, %(normal_beer_price)s, %(special_beer_price)s)")
    with CONNECTION.cursor() as cursor:
        for i in range(NUMBER_OF_PARTIES):
            year = random.randint(2017, 2018)
            month = random.randint(1, 12)
            day = random.randint(1, 28)
            party = {
                'name': 'Soirée ' + str(i),
                'date': date(year, month, day),
                'normal_beer_price': NORMAL_BEER_PRICE,
                'special_beer_price': SPECIAL_BEER_PRICE
            }
            cursor.execute(query, party)
    CONNECTION.commit()


def generate_beers():
    query = ("INSERT INTO products(name, price, type) "
             "VALUES(%(name)s, %(price)s, %(type)s)")
    with CONNECTION.cursor() as cursor:
        for i in range(NUMBER_OF_BEERS):
            beer = {
                "name": 'Bière ' + str(i),
                "price": random.uniform(0.9, 1.7),
                "type": "beer"
            }
            cursor.execute(query, beer)
    CONNECTION.commit()


def generate_deposits():
    query = ("INSERT INTO products(name, price, type) "
             "VALUES(%(name)s, %(price)s, %(type)s)")
    deposits = [
        {
            "name": "Caution demi",
            "price": 1,
            "type": "deposit"
        },
        {
            "name": "Caution pinte",
            "price": 2,
            "type": "deposit"
        },
        {
            "name": "Caution pichet",
            "price": 3,
            "type": "deposit"
        }
    ]
    with CONNECTION.cursor() as cursor:
        for deposit in deposits:
            cursor.execute(query, deposit)
    CONNECTION.commit()


def generate_food():
    query = ("INSERT INTO products(name, price, type) "
             "VALUES(%(name)s, %(price)s, %(type)s)")
    food_items = [
        {
            "name": "saucisson",
            "price": 3,
            "type": "food"
        }
    ]
    with CONNECTION.cursor() as cursor:
        for food in food_items:
            cursor.execute(query, food)
    CONNECTION.commit()


def generate_served_beers():
    query = ("INSERT INTO served_beers_at_party(party_id, product_id, category) "
             "VALUES(%(party_id)s, %(product_id)s, %(category)s)")
    categories = ['normal', 'special']
    with CONNECTION.cursor() as cursor:
        for party_id in range(1, NUMBER_OF_PARTIES + 1):
            used_beer_ids = []
            for i in range(NUMBER_OF_SERVED_BEERS_PER_PARTY):
                beer_id = random.randint(1, NUMBER_OF_BEERS)
                while beer_id in used_beer_ids:
                    beer_id = random.randint(1, NUMBER_OF_BEERS)

                served_beer = {
                    "party_id": party_id,
                    "product_id": beer_id,
                    "category": categories[random.randint(0,1)]
                }
                used_beer_ids.append(beer_id)
                cursor.execute(query, served_beer)
    CONNECTION.commit()


def generate_transactions():
    query = ("INSERT INTO transactions(member_id, party_id, amount, label, timestamp) "
             "VALUES(%(member_id)s, %(party_id)s, %(amount)s, %(label)s, %(timestamp)s)")
    with CONNECTION.cursor() as cursor:
        for party_id in range(1, NUMBER_OF_PARTIES + 1):
            for i in range(NUMBER_OF_TRANSACTIONS_PER_PARTY):
                if random.randint(0, 9) == 9:
                    label = "Recharge"
                    amount = random.randrange(5, 30, 5)
                else:
                    label = "Transaction " + str(i)
                    amount = random.randrange(-500, -150, 25)
                    amount = amount / 100.0
                transaction = {
                    "member_id": random.randint(1, MAX_MEMBER_ID),
                    "party_id": party_id,
                    "amount": amount,
                    "label": label,
                    "timestamp": datetime.now() + timedelta(minutes=i)
                }
                cursor.execute(query, transaction)
    CONNECTION.commit()


def generate_auth_token():
    # test token, hash for 'password'
    query = "INSERT INTO authentication_tokens " \
            "VALUES (0001, '$pbkdf2-sha256$29000$h5BSylnLOWdszTnHmLNWyg$hUHbZ2bC0mLgW7vB8puySWzfa0yIXZ5125etx19VVAw')"
    with CONNECTION.cursor() as cursor:
        cursor.execute(query)
    CONNECTION.commit()


print("Inserting parameters...")
generate_parameters()
print("Inserting parties...")
generate_parties()
print("Inserting beers...")
generate_beers()
print("Inserting deposits...")
generate_deposits()
print("Inserting food...")
generate_food()
print("Inserting served beers...")
generate_served_beers()
print("Inserting transactions...")
generate_transactions()
print("Inserting auth token...")
generate_auth_token()
print("DONE!")

CONNECTION.close()
