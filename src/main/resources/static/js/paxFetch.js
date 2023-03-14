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

    resp.forEach(arr => {
        arr.length > 0 && arr.forEach(el => {
            tBody.appendChild(createTR(el))
        })
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
    const editCallback = () => {
        createEditableTD(pax.fullName, tr, fullName)
        createEditableTD(paxDate, tr, birthDate)
        createEditableTD(pax.age, tr, age)
        createEditableTD(pax.email, tr, email)
        createEditableTD(pax.phone, tr, phone)
        createEditableTD(pax.egn, tr, egn)
        createEditableTD(pax.passportNumber, tr, passport)

        const tdSaveBtn = createTD()
        const btnSave = createTDButton(undefined, "Save")
        tdSaveBtn.appendChild(btnSave)
        tr.replaceChild(tdSaveBtn, tdEdit)

        const tdCancelBtn = createTD()
        const btnCancel = createTDButton(undefined, "Cancel")
        btnCancel.classList.add("bg-danger")
        tdCancelBtn.appendChild(btnCancel)
        tr.replaceChild(tdCancelBtn, tdDelete)

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

const createTDButton = (callback, text, customClasses) => {
    const btn = document.createElement("button")
    btn.classList.add("btn", "btn-info", "btn-sm")
    btn.textContent = text
    btn.onclick = callback
    return btn
}

const createEditableTD = (value, parentNode, childNode) => {
    const newTd = document.createElement("td")
    const inp = document.createElement("input")
    inp.classList.add("form-control")
    inp.value = value
    newTd.appendChild(inp)
    parentNode.replaceChild(newTd, childNode)
}


