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
	
		
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		
		
