export interface Proposal {
    person: Person
    address: Address
}

export interface Person {
    name: string;
    surname: string;
    email: string;
    pesel: string;
}

export interface Address {
    street: string
    city: string
}
