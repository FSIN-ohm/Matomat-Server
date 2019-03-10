#!/usr/bin/python3

import flask
from flask import Flask
from flask import Response

app = Flask(__name__)

def returnFileOnGet(filename):
    if flask.request.method == "GET":
        with open(filename) as file:
            return Response(file.read(), mimetype='application/json')
    else:
        return Response('', 200)

@app.route("/")
def hello():
    return "<html>This is the Matohmat REST intorface. Please read in the <a href=\"https://fsin-ohm.github.io/Matomat-Documentation/\">Documentation</a> how to use this.</html>"

##### USERS #####

@app.route("/users", methods=["GET", "POST"])
def users():
    return returnFileOnGet("users.json")

@app.route("/users/me")
def usersMe():
    return returnFileOnGet("user_me.json")

@app.route("/users/0", methods=["GET", "DELETE"])
def users0():
    return returnFileOnGet("user0.json")

##### ADMINS #####

@app.route("/admins", methods=["GET", "POST"])
def admins():
    return returnFileOnGet("admins.json")

@app.route("/admins/me")
def adminsMe():
    return returnFileOnGet("admin_me.json")

@app.route("/admins/0", methods=["GET", "PATCH", "DELETE"])
def admins0():
    return returnFileOnGet("admin0.json")

##### PRODUCTS #####

@app.route("/products", methods=["GET", "POST"])
def products():
    return returnFileOnGet("products.json")

@app.route("/products/0", methods=["GET", "PATCH", "DELETE"])
def product0():
    return returnFileOnGet("product0.json")

##### TRANSACTIONS #####

@app.route("/transactions", methods=["GET", "POST"])
def transactions():
    return returnFileOnGet("transactions.json")

@app.route("/transactions/0")
def transactions0():
    return returnFileOnGet("transaction0.json")

@app.route("/transactions/1")
def transactions1():
    return returnFileOnGet("transaction1.json")

@app.route("/transactions/2")
def transactions2():
    return returnFileOnGet("transaction2.json")

@app.route("/transactions/3")
def transactions3():
    return returnFileOnGet("transaction3.json")

if __name__ == '__main__':
    app.run(debug=True)

