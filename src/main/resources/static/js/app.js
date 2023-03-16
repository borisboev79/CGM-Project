
let loadBooksBtn = document.getElementById("loadBooks");

loadBooksBtn.addEventListener('click', reloadBooks)

// TODO: create new book

function reloadBooks() {

    let booksCntr = document.getElementById('guests-container')
    booksCntr.innerHTML = ''

    fetch("http://localhost:8080/api/guests/{id}").
    then(response => response.json()).
    then(json => json.forEach(guest => {
        // create row
        let row = document.createElement('tr')

        let cabNumCol = document.createElement('td')
        let cabCodeCol = document.createElement('td')
        let cabTypeCol = document.createElement('td')
        let fullNameCol = document.createElement('td')
        let emailCol = document.createElement('td')
        let phoneCol = document.createElement('td')
        let birthDateCol = document.createElement('td')
        let ageCol = document.createElement('td')
        let passportCol = document.createElement('td')
        let egnCol = document.createElement('td')



        cabNumCol.textContent = guest.cabinNumber
        cabCodeCol.textContent = guest.cabinCode
        cabTypeCol.textContent = guest.cabinType
        fullNameCol.textContent = guest.fullName
        emailCol.textContent = guest.email
        phoneCol.textContent = guest.phone
        birthDateCol.textContent = guest.birthDate
        ageCol.textContent = guest.age
        passportCol.textContent = guest.passportNumber
        egnCol.textContent = guest.egn

        row.appendChild(cabNumCol)
        row.appendChild(cabCodeCol)
        row.appendChild(cabTypeCol)
        row.appendChild(fullNameCol)
        row.appendChild(emailCol)
        row.appendChild(phoneCol)
        row.appendChild(birthDateCol)
        row.appendChild(ageCol)
        row.appendChild(passportCol)
        row.appendChild(egnCol)

        booksCntr.appendChild(row)
    }))

}
