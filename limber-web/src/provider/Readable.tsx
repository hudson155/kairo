import React from 'react';

type PromiseState = 'PENDING' | 'ERROR' | 'SUCCESS';

export default class Readable<T> {
  private status: PromiseState = 'PENDING';
  private result?: T;
  private readonly suspender: Promise<void>;

  constructor(promise: Promise<T>) {
    this.suspender = promise.then(
      result => {
        this.status = 'SUCCESS';
        this.result = result;
      },
      error => {
        this.status = 'ERROR';
        this.result = error;
      },
    );
  }

  read(): T {
    if (this.status === 'PENDING') throw this.suspender;
    if (this.status === 'ERROR') throw this.result;
    return this.result!; // TODO: No bang?
  }
}
