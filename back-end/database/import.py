#!/usr/bin/python3.5

import csv
import sys
import datetime
import unicodedata
import io

USER = 'root'
PORT = 3306
DATABASE = 'sbeereck-db'


def build_school_dictionnary(cvafile):
    dico = {}
    with open(cvafile) as csvfile:
        spamreader = csv.DictReader(csvfile)
        for row in spamreader:
            school = row['École']
            if school == 'Pagora':
                school = 'Papet'
            elif school == 'Genie industriel':
                school = 'GI'
            elif school == 'La Prepa':
                school = 'CPP'
            last_name = row['Nom']
            first_name = row['Prénom'].split('(', 1)[0]  # some names have notes written in ( ) attached to them
            dico[(last_name, first_name)] = school
    return dico


def get_members_from_csv(file, student_school_dict):
    members = []
    with open(file) as csvfile:
        spamreader = csv.DictReader(csvfile)
        for row in spamreader:
            last_name = row['Nom']
            first_name = row['Prénom']
            if len((last_name + first_name).replace(" ", "")) == 0:
                continue

            if (last_name, first_name) in student_school_dict:
                school = student_school_dict[(last_name, first_name)]
            else:
                school = ''

            last_membership_payment = datetime.datetime.now()
            is_former_staff = 0
            if row['Cotiz\''] == 'non':
                last_membership_payment.replace(year=last_membership_payment.year - 1)
            elif row['Cotiz\''] != 'oui': # member is part of the staff
                is_former_staff = 1

            s1 = last_name.replace(" ", "").lower() + '.' + first_name.replace(" ", "").lower()
            s2 = unicodedata.normalize('NFD', s1).encode('ascii', 'ignore')  # remove accents
            email = s2.decode('utf-8') + '@grenoble-inp.fr'

            member = {
                'first_name': first_name,
                'last_name': last_name,
                'school': school,
                'last_membership_payment': last_membership_payment,
                'email': email,
                'balance': row['Total'],
                'is_former_staff': is_former_staff
            }
            members.append(member)
    return members


# def insert_into_db(members, host, pwd):
#     cnx = mysql.connector.connect(user=USER, password=pwd, host=host, port=PORT, database=DATABASE)
#     cursor = cnx.cursor()
#     query = ("INSERT INTO members(first_name, last_name, school, email, balance, last_membership_payment, "
#              "is_former_staff) "
#              "VALUES(%(first_name)s, %(last_name)s, %(school)s, %(email)s, %(balance)s, %(last_membership_payment)s, "
#              "%(is_former_staff)s)")
#
#     for member in members:
#         cursor.execute(query, member)
#
#     cnx.commit()
#     cursor.close()
#     cnx.close()


if len(sys.argv) < 5:
    print("Usage: ./import_csv_members_do_db.py <csv members file> <csv CVA file> <host> <password>")
    exit(1)

dico = build_school_dictionnary(sys.argv[2])
res = get_members_from_csv(sys.argv[1], dico)
for m in res:
    print(m)
# insert_into_db(res, sys.argv[3],  sys.argv[4])
