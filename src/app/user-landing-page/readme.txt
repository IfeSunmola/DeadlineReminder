How the modal is shown:
    On click of "Add New Deadline" button, a function is called with the reference of the new-deadline-component (<app-new-deadline>) as
    its parameter

    The function then calls another function in the new-deadline-component to open the modal

    Starting from user-landing-page.html:
        1. The <app-new-deadline> tag is added, with a template ref.
        2. On click of the Add New Deadline button, openNewDeadlineModal() is called, with the template ref as its parameter
    Now in user-landing-page.ts:
        3. In openNewDeadlineModal, the template ref of <app-new-deadline> is used to call openModal() in new-deadline-component.ts
    Now in new-deadline-component.ts:
        4. @ViewChild('modalRef') is used to hold the reference of the actual modal.
            the reference in #1 above is a reference to the entire component/class of new-deadline-component
        5. Necessary initializations are done and openModal() finally opens the um ... modal


    The reason why I had to pass in a template ref to the function in #2 is because I couldn't find a way to directly access the modal in
    new-deadline-component.

