from BeautifulSoup import BeautifulSoup as bs
with open("PZSpecification.html") as blargh:
    ble = blargh.read()
    outt = open("pretty", "w")
    outt.write(bs(ble).prettify())
    outt.close()
