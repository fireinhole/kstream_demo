'use strict'

/** @type {typeof import('@adonisjs/lucid/src/Lucid/Model')} */
const Model = use('Model')

class Address extends Model {
  static get table() {
    return 'addresses';
  }

  static get createdAtColumn() {
    return null;
  }

  static get updatedAtColumn() {
    return null
  }

  get hisNew() {
    return this.id != undefined;
  }

  constructor() {
    super();

    this.address = "";
    this.city = "";
    this.country = "";
    this.postalcode = "";
  }
}

module.exports = Address
