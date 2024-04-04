# CardInformationFinder
 This is an application that checks information about your ATM card using the binlist API.

# Project Tree

```Bash

.
├── application
│   └── CardInfoFinderApplication.kt
├── data
│   ├── models
│   │   ├── CardBankInfo.kt
│   │   ├── CardCountryInfo.kt
│   │   └── CardInfoPage.kt
│   ├── providers
│   │   ├── remote
│   │   │   ├── AuthInterceptor.kt
│   │   │   └── CardInfoFinderApiList.kt
│   │   └── Url.kt
│   └── repository
│       ├── FetchCardInformationRepositoryImpl.kt
│       └── FetchCardInformationRepository.kt
├── di
│   ├── AuthModule.kt
│   └── NetworkStateModule.kt
├── ui
│   ├── base
│   │   └── BaseActivity.kt
│   ├── CardInformationDisplay.kt
│   ├── CardOcrConfirm.kt
│   ├── CardOptionSelection.kt
│   ├── CardProcessor.kt
│   └── viewModel
│       └── FetchCardInformationViewModel.kt
└── util
    ├── Constants.kt
    ├── CoroutinesHelper.kt
    ├── Event.kt
    ├── Extension.kt
    ├── NetworkManager.kt
    └── Resource.kt

12 directories, 23 files
```

# Description
This project has an implementation of the Model-View-ViewModel (MVVM) architecture pattern. It is designed to fetch and display card information through an application. The project structure is organized into different modules such as application logic, data handling, dependency injection, user interface, and utilities.

# Project Structure
## 1. application
Contains the main application file.

CardInfoFinderApplication.kt: Entry point of the application.
## 2. data
Handles data-related operations including models, data providers, and repositories.

### models:
Defines data models used within the application.

CardBankInfo.kt: Model for card bank information.
CardCountryInfo.kt: Model for card country information.
CardInfoPage.kt: Model for card information page.
providers
Contains classes responsible for providing data, both locally and remotely.

### remote:
Handles remote data retrieval and authentication.

AuthInterceptor.kt: Interceptor for authentication.
CardInfoFinderApiList.kt: API endpoints for card information.
Url.kt: Defines URLs used for network operations.

### repository:
Manages the data access layer of the application.

FetchCardInformationRepositoryImpl.kt: Implementation of the repository interface for fetching card information.
FetchCardInformationRepository.kt: Interface defining methods for fetching card information.

## 3. di
Contains classes related to dependency injection.

AuthModule.kt: Module for authentication-related dependencies.
NetworkStateModule.kt: Module for network state dependencies.

## 4. ui
Handles user interface components and logic.

### base
Contains base classes for UI components.

BaseActivity.kt: Base activity class.
CardInformationDisplay.kt: Screen for displaying card information.
CardOcrConfirm.kt: Screen for confirming OCR (Optical Character Recognition) data before proceeding.
CardOptionSelection.kt: Screen for selecting card operation options.
CardProcessor.kt: Component for processing card-related tasks after entrying card number directly.

### viewModel
Contains ViewModel classes responsible for managing UI-related data.

FetchCardInformationViewModel.kt: ViewModel for fetching card information.

### 5. util
Contains utility classes and helpers used throughout the project.

Constants.kt: Defines constant values used in the application.
CoroutinesHelper.kt: Helper class for managing coroutines.
Event.kt: Defines event handling mechanism.
Extension.kt: Contains extension functions.
NetworkManager.kt: Manages network-state related tasks.
Resource.kt: Defines resource handling mechanisms.

# Note: This is just a basic app, and it can still be improved using jetpack compose and more latest stuffs
