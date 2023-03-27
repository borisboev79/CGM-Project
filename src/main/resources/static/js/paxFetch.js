const tBody = document.getElementById("pax-info")
const fetchPax = async (id) => {
    const response = await fetch(`${hostName}/api/guests/${id}`, {
        method: "GET",
        headers: {
            'Content-type': 'application/json'
        }
    })
        .then(resp => {
            if (resp.status !== 200) {
                throw new Error("No passengers were found in this group!");
            }
            return resp.json()
        })
        .catch(err => {
            return { 'error': err.message};
        });

    if (!response.error) {
        response.forEach(pax => {
            tBody.appendChild(createTR(pax));
        })
    } else {
        tBody.appendChild(createTrError(response.error));
    }
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
        tdSaveBtn.classList.add("position-relative")
        const btnSave = createTDButton(async () => {
            const response = await sendUpdate(trData)
            if(!response.error) {
                pax = {...response}
                toggleCallback(response)
            } else {
                alertBox(response.error, tdSaveBtn, toggleCallback)
            }
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
    tdDelete.classList.add("position-relative")
    tdDelete.appendChild(createTDButton( async () => {

        const decision =  await confirmDialog()
        if (decision) {
            await sendDelete(pax.id, tBody, tr, tdDelete)
        }

    }, "Delete")).classList.add("bg-danger")
    tr.appendChild(tdDelete)

    return tr
}

const createTrError = (err) => {
    const tr = document.createElement("tr")
    const td = document.createElement("td")
    tr.classList.add("bg-danger", "text-white", "text-center")
    td.textContent = err
    td.setAttribute("colspan", "11")
    tr.appendChild(td)
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
    return new Date(formattedDate)
}

async function sendUpdate(data) {

    const csrfHeaderName = document.getElementById("csrf").getAttribute("name")
    const csrfHeaderToken = document.getElementById("csrf").getAttribute("value")

    return await fetch(`${hostName}/api/guests/edit`, {
        method: "PUT",
        body: JSON.stringify(data),
        headers: {
            'Content-type': 'application/json',
            [csrfHeaderName]: csrfHeaderToken
        }
    })
        .then(resp => {
            if(resp.status !== 200) {
                throw new Error("Unsuccessful update of passenger details. Please, try again later.")
            }
            return resp.json()
        })
        .catch(err => {
            return { 'error': err.message }
        });
}

async function sendDelete(id, parentNode, childNode, alertChild) {
    const csrfHeaderName = document.getElementById("csrf").getAttribute("name")
    const csrfHeaderToken = document.getElementById("csrf").getAttribute("value")

    await fetch(`${hostName}/api/guests/delete/${id}`, {
        method: "DELETE",
        headers: {
            'Content-type': 'application/json',
            [csrfHeaderName]: csrfHeaderToken
        }
    })
        .then(r => {
            if(r.status !== 200) {
                throw new Error("Error deleting passenger! They may have been already deleted.")
            }
            parentNode.removeChild(childNode)
            return
        })
        .catch(err => {
            alertBox(err.message, alertChild)
        });
}

async function confirmDialog() {
    const confirmContainer = document.createElement("div")
    confirmContainer.classList.add("confirm-container")

    const confirmBox = document.createElement("div")
    confirmBox.classList.add("confirm-box")

    const confirmText = document.createElement("div")
    confirmText.classList.add("text-white", "text-center")
    confirmText.textContent = "Are you sure you want to delete passenger?"


    confirmBox.appendChild(confirmText)
    const btnContainer = document.createElement("div")
    btnContainer.classList.add("confirm-btn-container")

    const btnConfirm = document.createElement("button")
    btnConfirm.textContent = "Yes, delete!"
    btnConfirm.classList.add("btn", "btn-info", "btn-md", "bg-danger")

    const btnCancel = document.createElement("button")
    btnCancel.textContent = "Cancel"
    btnCancel.classList.add("btn", "btn-info", "btn-md")

    const waitPromise = new Promise((resolve, reject) => {
        btnConfirm.addEventListener('click', resolve)
        btnCancel.addEventListener('click', reject)
    })

    function onConfirmCancel() {
        confirmContainer.classList.add("confirm-container-out")
        setTimeout(() => {
            document.body.removeChild(confirmContainer)
        }, 200)

    }

    async function waitResponse() {
        return await waitPromise
            .then(() => {
                onConfirmCancel()
                return true
            })
            .catch(() => {
                onConfirmCancel()
                return false
            })
    }

    btnContainer.appendChild(btnConfirm)
    btnContainer.appendChild(btnCancel)

    confirmBox.appendChild(btnContainer)

    confirmContainer.appendChild(confirmBox)

    document.body.appendChild(confirmContainer)
    return await waitResponse()
}

function alertBox(text, container, callBack) {
    const tooltip = document.createElement("div")
    tooltip.classList.add("error-tooltip")

    const tooltipText = document.createElement("span")
    tooltipText.classList.add("text-white", "text-center")
    tooltipText.textContent = text

    tooltip.appendChild(tooltipText)

    setTimeout(() => {
        tooltip.classList.add("error-out")
        setTimeout(() => {
            tooltip.classList.remove("error-out")
            container.removeChild(tooltip)
            callBack && callBack()
        }, 300)
    }, 3000)

    container.appendChild(tooltip)
}



