let currentPassword = document.getElementById('currentPassword')
let currentPasswordError = document.getElementById('currentPasswordError')

let newPassword = document.getElementById('newPassword')
let newPasswordError = document.getElementById('newPasswordError')

let confirmPassword = document.getElementById('confirmPassword')
let confirmPasswordError = document.getElementById('confirmPasswordError')

let saveBtn = document.getElementById("saveBtn")
let closeBtn = document.getElementById("closeBtn")
let passwordChangeForm = document.getElementById("passwordChangeForm")
let debounceTimer

let error = {
    cpE: true,
    npE: true,
    conpE: true
}

saveBtn.addEventListener('click', () => {
    closeBtn.click();
    passwordChangeForm.submit()
})

currentPassword.addEventListener('keyup', () => {
    checkCurrentP()
})

currentPassword.addEventListener('change', () => {
    handleCurrentPasswordInput()
})

async function checkPasswordAvailability(password) {

     let flag = await fetch(`http://localhost:8080/instructors/check-password?id=${theInstructorId.value}&password=${password}`)
        .then(response => response.json())
        .then(data => data['theResult'] );

    if (!flag) {
        currentPasswordError.innerText = 'Your password is wrong';
        error.cpE = true;
    } else {
        currentPasswordError.innerText = '';
        error.cpE = false;
        if (checkErrors()) {
            saveBtn.disabled = false
        }

    }

}

function handleCurrentPasswordInput() {
    // clearTimeout(debounceTimer)

    let password= document.getElementById('currentPassword').value
    checkPasswordAvailability(password)

    // debounceTimer = setTimeout(() => {
    //     checkPasswordAvailability(password)
    // }, 600)

}

function checkCurrentP() {
    if (currentPassword.value.length === 0) {
        currentPasswordError.innerText = 'Enter your current password'
        error.cpE = true
        saveBtn.disabled = true
    } else {
        currentPasswordError.innerText = ''
        error.cpE = false

        if (checkErrors()) {
            saveBtn.disabled = false
        }
    }

}

newPassword.addEventListener('keyup', (e) => {
    checkNewP()
    checkConPE()
});

function checkNewP() {
    if (newPassword.value.length < 8) {
        newPasswordError.innerText = 'At least 8 characters required'
        error.npE = true
        saveBtn.disabled = true
    } else {
        newPasswordError.innerText = ''
        error.npE = false

        if (checkErrors()) {
            saveBtn.disabled = false
        }
    }
}

confirmPassword.addEventListener('keyup', (e) => {
    checkConPE()
    checkNewP()
})

function checkConPE() {
    if (newPassword.value !== confirmPassword.value) {
        confirmPasswordError.innerText = "Passwords don't match"
        error.conpE = true
        saveBtn.disabled = true
    } else {
        confirmPasswordError.innerText = ""
        error.conpE =false

        if (checkErrors()) {
            saveBtn.disabled = false
        }
    }
}

function checkErrors() {
    return error.cpE === false && error.npE === false && error.conpE === false
}

/*
        Check Password Functionality
 */

let theInstructorId = document.getElementById("theInstructorId")


