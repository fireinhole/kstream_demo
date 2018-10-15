'use strict'

/** @type {typeof import('@adonisjs/lucid/src/Lucid/Model')} */
const Model = use('Model')

class Client extends Model {
  static get table () {
    return 'clients';
  }

    static get createdAtColumn () {
      return null;
    }

    static get updatedAtColumn () {
        return null
      }

  addresses() {
    return this.hasMany('App/Models/Address')
  }

  get hisNew() {
    return this.id != undefined;
  }

  constructor() {
    super();

    this.first_name = "";
    this.last_name = "";
    this.age = 30;
    this.gender = "M";
  }
}

module.exports = Client
