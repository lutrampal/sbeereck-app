# Endpoints

**All requests should include an 'authentication_token' key-value pair**  

## Parties
	* /parties  
		GET: returns all the parties, ordered my descending dates  
			[ {  
				"id":  
				"name":  
				"date":  
				"number_of_attendees":  
				"balance":  
			  }, ... ]  
		POST: insert a new party, the sent party object should be as follows  
			{
				"name":  
				"date":  
				"normal_beer_price":  
				"special_beer_price":  
				"served_beers":  
					[ {  
						"id":  
						"category":  
					  }, ... ]  
			}  
			the response, in case of success, contains the party id  
				{  
					id:  
				}  
				
	* /parties/<id>  
		GET : return the desired party
			{  
				"id":
				"name":  
				"date":  
				"normal_beer_price":  
				"special_beer_price":  
				"served_beers":  
					[ {  
						"id":  
						"name":  
						"category":
						"price":  
					  }, ... ]  
			}
		PUT : update the desired party, note that all served beers will be replaced by the sent ones
			{  
				"name":  
				"date":  
				"normal_beer_price":  
				"special_beer_price":  
				"served_beers":  
					[ {  
						"id":  
						"name":  
						"category":  
					  }, ... ]  
			}
		DELETE : delete the desired party, the deleted party is sent back.
			{  
				"id":  
				"name":  
				"date":  
				"number_of_attendees":  
				"balance":  
			}
	* /products
		GET : return all the products, a type parameter can be specified to get only the products of requested type.
			{
				"id":
				"name":
				"price":
				"type":
			}
	
		
	* /products/<id>
		DELETE : delete the desired product
		POST: insert a new product, the sent product object should be as follows  
			{
				"name":  
				"price":  
				"type":
			}  
			the response, in case of success, contains the product id  
				{  
					"id":  
				}  
		PUT: update a product, the sent product object should be as follows  
			{
				"name":  
				"price":  
				"type":
			}
	* /members
		GET return all the members	
			[ {  
				"id":  
				"first_name":  
				"last_name":  
				"balance":  
				"last_membership_payment":
				"is_former_staff":
			  }, ... ]  
		POST create a new member
			{
				"first_name":  
				"last_name":
				"school":
				"email":
				"phone":
			}
			the response, in case of success, contains the member id  
				{  
					"id":  
				}  

	* /members/<id>
		GET return the desired member	
			{  
				"id":  
				"first_name":  
				"last_name":  
				"balance":  
				"last_membership_payment":
				"school":
				"email":
				"phone":
				"is_former_staff":
			}
		DELETE delete the desired member
	* /members/<id>/membership
		PUT renew the membership of this member, no body is required, the last_membership_payment field will be set to the current timestamp
	* /parties/<id>/transactions
		GET return all the transactions for the desired party
			[ {  
				"id":  
				"member_id":
				"first_name":  
				"last_name":  
				"amount":  
				"label":
				"timestamp":
			  }, ... ]  
	* /parties/<id>/served_beers
		GET return all the served beers for the desired party
			[ {  
				"id":  
				"name":
				"category":  
				"last_name":  
				"amount":  
				"label":
				"timestamp":
			  }, ... ]  
	* /transactions/<id>
		DELETE delete the desired transaction
	* /transactions
		POST create a new transaction
			{
				"member_id":  
				"party_id": (optional, nullable)
				"label":
				"amount":
			}
			the response, in case of success, contains the transaction id  
				{  
					"id":  
				}  
	* /balance_too_low_threshold
		GET return the minimum accepted balance for an account
			{
				"balance_too_low_threshold":	
			}
		PUT update the minimum accepted balance for an account
			{
				"balance_too_low_threshold":	
			}
	* /default_prices/<product>
		GET return the default price for the requested product, valid options are "normal_beer" and "special_beer"
			{
				"default_product_price":	
			}
		PUT update the default price for the requested
			{
				"default_product_price":	
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		
		
