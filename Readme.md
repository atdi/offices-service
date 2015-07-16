### Run the project ### 

./gradlew bootRun

### End points ###

## Add a new office ##

POST /api/office

Json example:

{
    "name": "Paris",
    "city": "Paris",
    "country": "France",
    "openFrom": [
    8,
    0
  ],
  "openUntil": [
    18,
    0
  ]
}

## Modify an office ##
PUT /api/office/{id}

## Get an office ##
GET /api/office/{id}

## Get all offices ##
GET /api/office

## Get open offices ##
GET /api/office/open

## Get shortest route ##
GET /api/office/route