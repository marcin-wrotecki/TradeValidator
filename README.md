# TradeValidator

The main purpose of the application is to validate the trades for the FOREX transactions = FX Spot, Forward, Options. <br />

<h2>Endpoints</h2>
<strong>/trade/validate</strong> - validates FX Spot or FX Forward trade   <br />
Example input:   <br />
{   <br />
"customer": "YODA1",   <br />
"ccyPair": "EURUSD",   <br />
"type": "Spot",   <br />
"direction": "BUY",   <br />
"tradeDate": "2020-08-11",   <br />
"amount1": 1000000.00,   <br />
"amount2": 1120000.00,   <br />
"rate": 1.12,   <br />
"valueDate": "2020-08-15",   <br />
"legalEntity": "UBS AG",   <br />
"trader": "Josef Schoenberger"   <br />
} <br />
 <br />
<strong>/trades/validate</strong> - validates collection of FX Spot or FX Forward trades   <br />
Example input:   <br />
[ <br />
{
"customer": "YODA1", <br />
"ccyPair": "EURUSD", <br />
"type": "Spot", <br />
"direction": "BUY", <br />
"tradeDate": "2020-08-11", <br />
"amount1": 1000000.00, <br />
"amount2": 1120000.00, <br />
"rate": 1.12, <br />
"valueDate": "2020-08-15", <br />
"legalEntity": "UBS AG", <br />
"trader": "Josef Schoenberger" <br />
}, <br />
{ <br />
"customer": "YODA1", <br />
"ccyPair": "EURUSD", <br />
"type": "Spot", <br />
"direction": "SELL", <br />
"tradeDate": "2020-08-11", <br />
"amount1": 1000000.00, <br />
"amount2": 1120000.00, <br />
"rate": 1.12, <br />
"valueDate": "2020-08-22", <br />
"legalEntity": "UBS AG", <br />
"trader": "Josef Schoenberger" <br />
} <br />
] <br />

<strong>/trade/option/validate</strong> - validates FX Option trade <br />
Example input: <br />
{ <br />
"customer": "YODA1", <br />
"ccyPair": "EURUSD", <br />
"type": "VanillaOption", <br />
"style": "EUROPEAN", <br />
"direction": "BUY", <br />
"strategy": "CALL", <br />
"tradeDate": "2020-08-11", <br />
"amount1": 1000000.00, <br />
"amount2": 1120000.00, <br />
"rate": 1.12, <br />
"deliveryDate": "2020-08-22", <br />
"expiryDate": "2020-08-19", <br />
"payCcy": "USD", <br />
"premium": 0.20, <br />
"premiumCcy": "USD", <br />
"premiumType": "%USD", <br />
"premiumDate": "2020-08-12", <br />
"legalEntity": "UBS AG", <br />
"trader": "Josef Schoenberger" <br />
} <br />

<strong>/trades/option/validate</strong> - validates collection of FX Option trades <br />
Example input: <br />
[ <br />
{ <br />
"customer": "YODA1", <br />
"ccyPair": "EURUSD", <br />
"type": "VanillaOption", <br />
"style": "EUROPEAN", <br />
"direction": "BUY", <br />
"strategy": "CALL", <br />
"tradeDate": "2020-08-11", <br />
"amount1": 1000000.00, <br />
"amount2": 1120000.00, <br />
"rate": 1.12, <br />
"deliveryDate": "2020-08-22", <br />
"expiryDate": "2020-08-19", <br />
"payCcy": "USD", <br />
"premium": 0.20, <br />
"premiumCcy": "USD", <br />
"premiumType": "%USD", <br />
"premiumDate": "2020-08-12", <br />
"legalEntity": "UBS AG", <br />
"trader": "Josef Schoenberger" <br />
}, <br />
{ <br />
"customer": "YODA1", <br />
"ccyPair": "EURUSD", <br />
"type": "VanillaOption", <br />
"style": "AMERICAN", <br />
"direction": "BUY", <br />
"strategy": "CALL", <br />
"tradeDate": "2020-08-11", <br />
"amount1": 1000000.00, <br />
"amount2": 1120000.00, <br />
"rate": 1.12, <br />
"deliveryDate": "2020-08-22", <br />
"expiryDate": "2020-08-19", <br />
"excerciseStartDate": "2020-08-12", <br />
"payCcy": "USD", <br />
"premium": 0.20, <br />
"premiumCcy": "USD", <br />
"premiumType": "%USD", <br />
"premiumDate": "2020-08-12", <br />
"legalEntity": "UBS AG", <br />
"trader": "Josef Schoenberger" <br />
} <br />
] <br />

# Approach for providing high availability of the service and its scalability

To provide high availability and scalability it would be a good idea to start many instances of the application. If you use Cloud - create redundant hosts in multi-region. What is more, ensure constant monitoring of traffic to find service errors. <br />
To synchronize instances and ensure automatic multisite failover you can use Eureka Server and Zuul Gateway. These tools make it easy to maintain and develop. Remember that the application uses a database.<br />
If you would like to add further microservices, use Config Server to synchronize .properties files.<br/>
It is worth mentioning that we have horizontal and vertical scaling. Horizontal scaling refers to adding additional nodes, vertical scaling describes adding more power to your current machines. <br />
Choose the option that suits you.
