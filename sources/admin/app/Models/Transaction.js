'use strict'

/** @type {typeof import('@adonisjs/lucid/src/Lucid/Model')} */
const Model = use('Model')

class Transaction extends Model {
  static get table() {
    return 'transactions';
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

    this.amount = 0;
    this.type = "";
  }
}

module.exports = Transaction
