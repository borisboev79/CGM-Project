const hostName = "http://localhost:8080";
const tBody = document.getElementById("pax-info")
const fetchPax = async (id) => {
    const response = await fetch(`${hostName}/api/guests/${id}`, {
        method: "GET",
        headers: {
            'Content-type': 'application/json'
        }
    });
    const resp = await response.json();

    resp.forEach(el => {
            tBody.appendChild(createTR(el))
        })

    showHideContainer(true);
}

const showHideContainer = (show) => {
    const container = document.getElementById("pax-list");
    if (show) {
        container.classList.remove("d-none");
        document.getElementById("show-pax-btn").classList.add("d-none");
        document.getElementById("hide-pax-btn").classList.remove("d-none");
    } else {
        container.classList.add("d-none")
        document.getElementById("hide-pax-btn").classList.add("d-none");
        document.getElementById("show-pax-btn").classList.remove("d-none");
        tBody.innerHTML = '';
    }
}

const createTR = (pax) => {
    const tr = document.createElement("tr");
    const inputs = {}
    const editCallback = () => {
        const editFullName = createEditableTD(pax.fullName, tr, fullName, "inputFullName", validateFullName)
        const editBirthDate = createEditableTD(paxDate, tr, birthDate, "inputBirthDate", validateBirthDate)
        const editAge = createEditableTD(pax.age, tr, age)
        const editEmail= createEditableTD(pax.email, tr, email)
        const editPhone = createEditableTD(pax.phone, tr, phone)
        const editEgn = createEditableTD(pax.egn, tr, egn)
        const editPassport = createEditableTD(pax.passportNumber, tr, passport)

        const tdSaveBtn = createTD()
        const btnSave = createTDButton(undefined, "Save")
        tdSaveBtn.appendChild(btnSave)
        tr.replaceChild(tdSaveBtn, tdEdit)

        const tdCancelBtn = createTD()
        const btnCancel = createTDButton(cancelCallback, "Cancel")
        btnCancel.classList.add("bg-danger")
        tdCancelBtn.appendChild(btnCancel)
        tr.replaceChild(tdCancelBtn, tdDelete)

        function cancelCallback() {
            tr.replaceChild(fullName, editFullName.td)
            tr.replaceChild(birthDate, editBirthDate.td)
            tr.replaceChild(age, editAge.td)
            tr.replaceChild(email, editEmail.td)
            tr.replaceChild(phone, editPhone.td)
            tr.replaceChild(egn, editEgn.td)
            tr.replaceChild(passport, editPassport.td)
            tr.replaceChild(tdEdit, tdSaveBtn)
            tr.replaceChild(tdDelete, tdCancelBtn)
        }

        function createEditableTD(value, parentNode, childNode, inputName, validate) {
            const newTd = document.createElement("td")
            const inp = document.createElement("input")
            inputs[inputName] = {
                valid: true,
                value: value
            }
            inp.classList.add("form-control")
            inp.value = value
            inp.oninput = () => {
                const inputValue = inp.value
                if (!validate(inputValue)) {
                    inp.classList.add("bg-danger")
                    inputs[inputName].valid = false

                } else {
                    inp.classList.remove("bg-danger")
                    inputs[inputName].valid = true
                }
                inputs[inputName].value = inputValue

                let validInputs = true

                for (const v of Object.values(inputs)) {
                    if(!v.valid) {
                        validInputs = false
                        break
                    }
                }

                if (validInputs) {
                    btnSave.removeAttribute("disabled")
                } else {
                    btnSave.setAttribute('disabled', true)
                }
            }
            newTd.appendChild(inp)
            parentNode.replaceChild(newTd, childNode)

            return { td: newTd, input: inp}
        }
    }

    tr.appendChild(createTD(pax.cabinNumber))
    tr.appendChild(createTD(pax.cabinCode))
    tr.appendChild(createTD(pax.cabinType))
    const fullName = createTD(pax.fullName)
    tr.appendChild(fullName)
    const paxDate = new Date(pax.birthDate).toLocaleDateString()
    const birthDate = createTD(paxDate)
    tr.appendChild(birthDate)
    const age = createTD(pax.age)
    tr.appendChild(age)
    const email = createTD(pax.email)
    tr.appendChild(email)
    const phone = createTD(pax.phone)
    tr.appendChild(phone)
    const egn = createTD(pax.egn)
    tr.appendChild(egn)
    const passport = createTD(pax.passportNumber)
    tr.appendChild(passport)
    const tdEdit = createTD()
    const btnEdit = createTDButton(editCallback, "Edit")

    tdEdit.appendChild(btnEdit)
    tr.appendChild(tdEdit)
    const tdDelete = createTD()
    tdDelete.appendChild(createTDButton(undefined, "Delete")).classList.add("bg-danger")
    tr.appendChild(tdDelete)

    return tr
}

const createTD = (value) => {
    const tdData = document.createElement("td")
    tdData.textContent = value
    return tdData;
}

const createTDButton = (callback, text) => {
    const btn = document.createElement("button")
    btn.classList.add("btn", "btn-info", "btn-sm")
    btn.textContent = text
    btn.onclick = callback
    return btn
}

function validateFullName(value) {
    return !(!value || value.length < 7);
}

function validateBirthDate(value){
    if (!value) {
        return false
    }
    const dateParts = value.split("/")
    const formattedDate = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2]
    const date = new Date(formattedDate)

    return date <= Date.now();
}



