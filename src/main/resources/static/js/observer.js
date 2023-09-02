// observer.js
class Subject {
    constructor() {
        this.observers = [];
    }

    addObserver(observer) {
        this.observers.push(observer);
    }

    removeObserver(observer) {
        const index = this.observers.indexOf(observer);
        if (index !== -1) {
            this.observers.splice(index, 1);
        }
    }

    notify(data) {
        this.observers.forEach(observer => {
            observer.update(data);
        });
    }
}

class EditableElement {
    constructor(elementId) {
        this.element = document.getElementById(elementId);
    }

    enableEditing() {
        this.element.contentEditable = true;
    }

    disableEditing() {
        this.element.contentEditable = false;
    }
}

const subject = new Subject();

export { subject, EditableElement };
