const hostName = "http://localhost:8080";
const tBody = document.getElementById("pax-info")
const fetchPax = async (id) => {
    const response = await fetch(`${hostName}/api/guests/${id}`, {
        method: "GET",
        headers: {
            'Content-type': 'application/json'
        }
    })
        .catch(err => {
            console.log("Error SHIT, ", err)
        })
    const resp = await response.json();
    resp.forEach(pax => {
        tBody.appendChild(createTR(pax))
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
    let trData = {...pax}
    const editCallback = () => {

        const editFullName = createEditableTD(pax.fullName, tr, fullName, "fullName", validateFullName)
        const editBirthDate = createEditableTD(paxDate, tr, birthDate, "birthDate", validateBirthDate)
        const editEmail= createEditableTD(pax.email, tr, email, "email", validateEmail)
        const editPhone = createEditableTD(pax.phone, tr, phone, "phone", validatePhone)
        const editEgn = createEditableTD(pax.egn, tr, egn, "egn", validateEgn)
        const editPassport = createEditableTD(pax.passportNumber, tr, passport, "passportNumber", validatePassport)

        const tdSaveBtn = createTD()
        const btnSave = createTDButton(async () => {
            console.log("new data", trData)
            const response = await sendUpdate(trData)
            pax = {...response}
            toggleCallback(response)
        }, "Save")

        tdSaveBtn.appendChild(btnSave)
        tr.replaceChild(tdSaveBtn, tdEdit)

        const tdCancelBtn = createTD()
        const btnCancel = createTDButton(() => toggleCallback(undefined), "Cancel")
        btnCancel.classList.add("bg-danger")
        tdCancelBtn.appendChild(btnCancel)
        tr.replaceChild(tdCancelBtn, tdDelete)

        function toggleCallback(responseData) {
            if (responseData) {
                fullName.textContent = responseData.fullName
                const paxDate = new Date(responseData.birthDate).toLocaleDateString()
                birthDate.textContent = paxDate
                email.textContent = responseData.email
                phone.textContent = responseData.phone
                egn.textContent = responseData.egn
                passport.textContent = responseData.passportNumber
            }
            console.log("fullName", fullName)
            tr.replaceChild(fullName, editFullName.td)
            tr.replaceChild(birthDate, editBirthDate.td)
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
                    let newValue;
                    if(inputName === 'birthDate') {
                        newValue = dateConverter(inputs[inputName].value).toISOString()
                        console.log("newValue", newValue)
                    } else {
                        newValue = inputs[inputName].value
                    }
                    trData = {
                        ...trData,
                        [inputName]: newValue
                    }
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
    tdDelete.appendChild(createTDButton(async () => {
        await sendDelete(pax.id, tBody, tr)
    }, "Delete")).classList.add("bg-danger")
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

    const date = dateConverter(value)

    return date <= Date.now();
}
const validateEmail = (email) => {
    return String(email)
        .toLowerCase()
        .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
};

function validatePhone(value) {
    return !(!value.match(/^[+]?\d+$/) || value.length < 9);
}

function validatePassport(value) {
    return !(value.length < 5);
}

function validateEgn(value)
{
    return !(!value.match(/^\d+$/) || value.length !== 10);
}

function dateConverter(value) {
    const dateParts = value.split("/")
    const formattedDate = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2]
    const date = new Date(formattedDate)
    return date
}

async function sendUpdate(data) {

    const csrfHeaderName = document.getElementById("csrf").getAttribute("name")
    const csrfHeaderToken = document.getElementById("csrf").getAttribute("value")

    const resp = await fetch(`${hostName}/api/guests/edit`, {
        method: "PUT",
        body: JSON.stringify(data),
        headers: {
            'Content-type': 'application/json',
            [csrfHeaderName]: csrfHeaderToken
        }
    })
        .catch(err => {
            console.log("Error SHIT, ", err)
        });
    const jsonResp = await resp.json();
    return jsonResp;
}

async function sendDelete(id, parentNode, childNode) {
    const csrfHeaderName = document.getElementById("csrf").getAttribute("name")
    const csrfHeaderToken = document.getElementById("csrf").getAttribute("value")

    const resp = await fetch(`${hostName}/api/guests/delete/${id}`, {
        method: "DELETE",
        headers: {
            'Content-type': 'application/json',
            [csrfHeaderName]: csrfHeaderToken
        }
    })
        .then(r => {
            if(r.status === 200) {
                parentNode.removeChild(childNode)
            } else {
                console.log("Something happened")
            }
        })
        .catch(err => {
            console.log("Error SHIT, ", err)
        });
}



