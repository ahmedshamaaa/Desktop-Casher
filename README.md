# Desktop-Casher
# The UI will have the following design:
**1- is a text label that shows the current invoice number.**
  
  - **Invoice number is serial number that gets incremented by 1 after an invoice is finalised and inserted into the database.**
  
**2- is a list of the items that are in the invoice, each element in The list should show the item barcode, name, and price.**
  
**3- is a text field where the user will type the barcode and then Press then Enter key. If the barcode matches a barcode in the items table then the item will be added the list #2, else an error message should be shown to the user.**
  
**4- is a button that the user presses after they are done adding items to the invoice. It should show a new window where the invoice can be finalised and inserted into the database.**

**5- is a text field where the user will enter the cash amount the customer will pay (100 EGP, 150 EGP etc.).**

**6- is a text label that shows the invoice total, which is the sum of all the invoice items' prices.**

**7- is a text label that shows the difference between the paid (#S) and total (#6) fields.**

**8- is a button which when pressed the invoice will be inserted into the database, the payment window closed, current invoice number (#1) incremented, and list (#2) emptied. If the paid (#5) is less than the total (#6), an error message is shown. after finalising the invoice the invoice data (number, total, paid, change and items) are sent to the printer in any suitable format.**
