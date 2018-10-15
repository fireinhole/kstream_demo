'use strict'

/*
|--------------------------------------------------------------------------
| Routes
|--------------------------------------------------------------------------
|
| Http routes are entry points to your web application. You can create
| routes for different URLs and bind Controller actions to them.
|
| A complete guide on routing is available here.
| http://adonisjs.com/docs/4.0/routing
|
*/

/** @type {import('@adonisjs/framework/src/Route/Manager'} */
const Route = use('Route')
const Client = use('App/Models/Client')
const Address = use('App/Models/Address')
const Transaction = use('App/Models/Transaction')
const UUID = use('App/UUID')


Route.get('/', async ({view}) => {
  const allClients = await Client.all();
  return await view.render('home', { clients : allClients.toJSON() });
})

Route.get('/views/addclient', async ({view}) => {
  return await view.render('addclient', { client: new Client() });
})

Route.get('/views/addclient/:id', async ({view, params}) => {
  const theClient = await await Client.find(params.id);
  return await view.render('addclient', { client: theClient });
})

Route.get('/views/transactions', async ({view}) => {
  const allClients = await Client.all();
  return await view.render('transactions', { clients : allClients.toJSON() });
})

Route.get('/views/addaddress/:clientid', async ({view, params}) => {
  return await view.render('addaddress', { clientid: params.clientid, address: new Address() });
})

Route.get('/views/addaddress/:clientid/:addressid', async ({view, params}) => {
  var address = await Address.find(params.addressid);
  return await view.render('addaddress', { clientid: params.clientid, address: address});
})

Route.get('/views/:id', async ({params, view}) => {
  const theClient = await Client.query().where('id', '=', params.id).with('addresses').fetch();
  return await view.render('client', { client : theClient.toJSON()[0] });
})

Route.get('clients', async () => {
  return await Client.query().with('addresses').fetch();
})

Route.get('clients/:id', async ({params}) => {
  return await Client.query().where('id', '=', params.id).with('addresses').fetch();
})

Route.get('clients/:id/addresses', async ({params}) => {
  return await Address.query().where('client_id', '=', params.id).fetch();
})

Route.post('clients/:id/addresses', async ({view, params, request, response}) => {
  const theClient = await Client.find(params.id);
  const postedAddress = request.post();

  var isCreate = ((postedAddress.id == undefined) || (postedAddress.id.length == 0) || (postedAddress.id == "undefined"));

  var address = undefined;
  var addressID = undefined;
  if (isCreate) {
    address = new Address();
    addressID = UUID.newUUID();
  }
  else {
    address = await Address.find(postedAddress.id);
    addressID = postedAddress.id;
  }

  address.fill(postedAddress);
  address.id = addressID;

  await theClient.addresses().save(address);

  if ((request.request.headers["content-type"] != undefined) && (request.request.headers["content-type"] == "application/json")) {
    return addressID;
  }

  response.redirect('/views/' + theClient.id);
})

Route.get('clients/:clientID/addresses/:id', async ({params}) => {
  return await Address.query().where('id', '=', params.id).fetch();
})

Route.post('clients', async ({view, request, response}) => {
  var postedClient = request.post();

  var isCreate = ((postedClient.id == undefined) || (postedClient.id.length == 0) || (postedClient.id == "undefined"));
  var client = null;
  if (isCreate) {
    postedClient.id = UUID.newUUID();

    client = new Client();
  }
  else {
    client = await Client.find(postedClient.id);
  }

  client.fill(postedClient);

  await client.save();
 
  if ((request.request.headers["content-type"] != undefined) && (request.request.headers["content-type"] == "application/json")) {
    return postedClient;
  }

  response.redirect('/views/' + postedClient.id);
})

Route.post('transactions', async ({view, request, response}) => {
  const trRequest = request.post();

  const types = trRequest.types.split(";");
  const clientID = trRequest.client;
  var clientIDS = null;
  if (clientID == 'all') {
    clientIDS = new Array();
    const allClients = await Client.all();
    allClients.toJSON().forEach(element => {
      clientIDS.push(element.id);
    });
  } else {
    clientIDS = [ clientID ];
  }

  const maxClientIDS = clientIDS.length;
  const maxTypes = types.length;
  for (var count = 0; count < trRequest.count; count++) {
    const clientIDX = Math.floor((Math.random() * maxClientIDS));
    const typeIDX = Math.floor((Math.random() * maxTypes));

    var transaction = new Transaction();
    transaction.id = UUID.newUUID();
    transaction.client_id = clientIDS[clientIDX];
    transaction.type = types[typeIDX];
    transaction.amount = Math.random() * 1000;

    await transaction.save();
  }

  response.redirect('/');
})

