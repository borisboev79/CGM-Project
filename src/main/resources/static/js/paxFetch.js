const hostName = "http://localhost:8080";
const fetchPax = async (id) => {
    const response = await fetch(`${hostName}/api/guests/${id}`, {
        method: "GET",
        headers: {
            'Content-type': 'application/json'
        }
    });
    const resp = await response.json();

    const tBody = document.getElementById("pax-info")
    resp.forEach(arr => {
        arr.length > 0 && arr.forEach(el => {
            tBody.appendChild(createTR(el))
        })
    })
    showHideContainer(true);
    console.log('resp', resp);
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

    }
}

const createTR = (pax) => {
    const tr = document.createElement("tr");

    tr.appendChild(createTD(pax.cabinNumber))
    tr.appendChild(createTD(pax.cabinCode))
    tr.appendChild(createTD(pax.cabinType))
    tr.appendChild(createTD(pax.fullName))
    tr.appendChild(createTD(pax.birthDate))
    tr.appendChild(createTD(pax.age))
    tr.appendChild(createTD(pax.email))
    tr.appendChild(createTD(pax.phone))
    tr.appendChild(createTD(pax.egn))
    tr.appendChild(createTD(pax.passportNumber))
    const tdEdit = createTD()
    tdEdit.appendChild(createTDButton("Edit"))
    tr.appendChild(tdEdit)
    const tdDelete = createTD()
    tdDelete.appendChild(createTDButton("Delete"))
    tr.appendChild(tdDelete)
    return tr
}

const createTD = (value) => {
    const tdData = document.createElement("td")
    tdData.textContent = value
    return tdData;
}

const createTDButton = (text) => {
    const btn = document.createElement("button")
    btn.classList.add("btn", "btn-info", "btn-sm")
    btn.textContent = text
    return btn
}

