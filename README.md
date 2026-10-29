#paypal-express-checkout
Demo for paypal-express-checkout

Requires Java 17 or newer.

The server enforces the displayed camera offer (quantity 1, total 17) and rejects altered prices or fees. Return callbacks use the stored checkout and must match its server-issued token. Checkout and response dumps are omitted from application logs.

Amounts use decimal arithmetic, bounded magnitude, currency precision, and negative shipping discounts. Unit prices and order item totals follow the [PayPal NVP field definitions](https://developer.paypal.com/api/nvp-soap/set-express-checkout-nvp/). Tests cover invalid local HTTP requests and model calculations; no PayPal payment is executed.


from: https://demo.paypal.com/us/demo/download?capability=checkOutWithPaypal&lang=Java


about paypal parameters info: https://github.com/tinywind/paypal-express-checkout/blob/master/README.html
