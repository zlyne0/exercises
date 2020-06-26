export class Student {
    id: number;
    firstname: string;
    lastname: string;
    birthdate: string;

    constructor(id: number, firstname : string, lastname: string, birthdate : string) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }
}