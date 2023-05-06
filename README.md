# Nosh: On-Campus Food Delivery Service

Nosh is an on-campus food delivery service designed for students, faculty, and employees of USF. The app enables users to have their favorite dining options delivered right to their location by bike. This service is perfect for those who don't want to lose their favorite spot in the library, those with a tight commute between classes during lunchtime, or employees who can't leave their workplace to grab food.

## Requirements

- JDK 1.8 or higher
- Android Studio

## Getting Started

1. Clone the repository and open the project in Android Studio.
2. Run the application on an emulator or a physical device with GPS capabilities.
3. Register for an account using an email and password.
4. Browse the available menus (currently, only Subway is implemented for demonstration purposes) and add items to your shopping cart.
5. Place your order and track its location and status in real-time.

## Features

- User registration and authentication
- Browse available menus and select items to add to the shopping cart
- Place orders and make payments (for demonstration purposes only)
- Real-time tracking of order location and status for customers
- For employees (drivers), the ability to accept an order and mark it as delivered
- Support for multiple items per order

## Limitations

- Only Subway's menu is implemented for demonstration purposes
- Orders cannot be canceled
- Drivers can only pick up one order at a time
- Users can only make one order at a time
- GPS location services might not work on emulators without GPS capabilities or without pre-loaded simulated locations. To avoid this issue, use a physical device or choose simulated locations for your emulator.

## Developers

- Laith Abdel-Rahman
- Dylan Bradford
- Ayman Nagi
# ExampleInstrumentedTest.java

The `ExampleInstrumentedTest` class demonstrates how to create an instrumented test, which will execute on an Android device. This test verifies that the app's context is correctly set up and its package name matches the expected value.

## Dependencies

- androidx.test.platform.app.InstrumentationRegistry
- androidx.test.ext.junit.runners.AndroidJUnit4
- org.junit.Test
- org.junit.runner.RunWith
- org.junit.Assert

## Test Class

```java
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.android.nosh", appContext.getPackageName());
    }
}
```

### Annotations

- `@RunWith(AndroidJUnit4.class)`: Specifies that this test class should use the `AndroidJUnit4` test runner.
- `@Test`: Marks the `useAppContext()` method as a test method to be executed by the test runner.

### Test Methods

#### useAppContext()

The `useAppContext()` method retrieves the app's context and checks if the package name of the app matches the expected value, "com.android.nosh".

1. Obtain the context of the app under test:

```java
Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
```

2. Assert that the package name of the app matches the expected value:

```java
assertEquals("com.android.nosh", appContext.getPackageName());
```

## Usage

To run this instrumented test, you'll need to have an Android device (physical or emulator) connected and set up for testing. In Android Studio, right-click on the `ExampleInstrumentedTest` class and select "Run 'ExampleInstrumentedTest'" from the context menu. The test will be executed on the connected device, and the results will be displayed in the "Run" window.
# google_maps_api.xml

The `google_maps_api.xml` file is a resource file that stores the Google Maps API key for your Android application. The Google Maps API key is required to access and use Google Maps services within your app.

## Acquiring a Google Maps API Key

Before running your application, you need to acquire a Google Maps API key. To do so, follow these steps:

1. Go to the Google Cloud Platform Console: https://console.developers.google.com/
2. Click on the project drop-down and select or create the project for which you want to add an API key.
3. Click the hamburger menu and select APIs & Services > Dashboard.
4. Click the "+ ENABLE APIS AND SERVICES" button, search for "Maps SDK for Android," and enable it.
5. Click the "Credentials" tab, then click "Create credentials," and select "API key" from the dropdown menu.
6. Copy the generated API key (it starts with "AIza").

## Adding the Google Maps API Key to your App

Once you have your API key, replace the "google_maps_key" string in the `google_maps_api.xml` file:

```xml
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">YOUR_API_KEY</string>
```

Replace `YOUR_API_KEY` with the API key you obtained from the Google Cloud Platform Console.

## Important Note

Make sure not to share your API key publicly, as it grants access to Google Maps services, which can incur charges. When sharing your project, it's recommended to remove the API key and provide instructions for others to obtain and use their own API keys.

## Package Name and SHA-1 Certificate Fingerprint

The `google_maps_api.xml` file also provides information about the package name and SHA-1 certificate fingerprint required for the API key. Ensure that the package name and SHA-1 certificate fingerprint match those of your app.

```xml
Package name:
com.android.nosh.ui.customer

SHA-1 certificate fingerprint:
A9:70:22:37:EE:9B:A8:54:02:C9:DA:F5:03:61:F1:39:8C:B3:82:D0
```

Follow the instructions in the comments of the `google_maps_api.xml` file or the Google Maps documentation to add the package name and SHA-1 certificate fingerprint to your API key: https://developers.google.com/maps/documentation/android/start#get-key

## Usage

Once you have added the Google Maps API key to your app, you can use Google Maps services in your Android application. The API key will be automatically used when you initialize Google Maps components in your app.

# CartActivity.java

`CartActivity` is an Android Activity class that handles the user's shopping cart. This activity displays the items in the user's cart, calculates the subtotal, tax, delivery fee, and total cost, and allows the user to remove items from the cart or proceed to order.

## Class Members

- `TextView subTotTxtView, taxTxtView, deliveryFeeTxtView, totalTxtView`: TextViews to display the subtotal, tax, delivery fee, and total cost.
- `ImageButton homeButton`: Button to navigate back to the home screen.
- `Button purchaseButton, removeCart`: Buttons to purchase the order and remove items from the cart.
- `RecyclerView rvCartItems`: RecyclerView to display the cart items.
- `Map<String, CheckBox> listOfCheckBoxes`: A map containing the CheckBoxes for each item in the cart.
- `List<Double> prices`: A list containing the subtotal, tax, and total cost.
- `FirebaseRecyclerOptions<MenuItem> options`: FirebaseRecyclerOptions for querying the cart items.
- `FirebaseRecyclerAdapter<MenuItem, CartItemHolder> adapter`: FirebaseRecyclerAdapter for managing the cart items.

## onCreate()

This method is called when the activity is first created. It initializes the activity layout, Firebase references, RecyclerView, and its adapter. It also sets up click listeners for the home button, purchase button, and remove button.

## onStart() and onStop()

These methods are used to start and stop the FirebaseRecyclerAdapter's listening, which updates the cart items displayed in the RecyclerView.

## getTotal()

This method calculates the subtotal, tax, and total cost of the items in the cart. It returns a list containing these values.

## removeFromCart()

This method removes the checked items from the cart and updates the UI accordingly.

## goOrder()

This method creates a new DeliveryOrder, adds it to the order queue, updates the delivery status, clears the cart, and navigates to the `CustomerOrderActivity`.

## goHome()

This method navigates back to the `CustomerActivity`.

## CartItemHolder

This is an inner class that extends RecyclerView.ViewHolder. It is used to hold the views for each cart item displayed in the RecyclerView. It contains TextViews for item name, item combo status, and item cost, as well as a CheckBox for selecting items to be removed from the cart.

# CustomerActivity.java

`CustomerActivity` is an Android Activity class that serves as the main screen for customers in the app. It displays the user's name and provides buttons to start an order, track an existing order, and view the shopping cart.

## Class Members

- `TextView nameView`: TextView to display the user's name.
- `Button startOrder, trackOrder`: Buttons to start a new order and track an existing order.
- `ImageButton cartButton`: Button to navigate to the shopping cart.
- `String userName`: The name of the current user.
- `FirebaseAuth mAuth`: Firebase authentication instance.
- `DatabaseReference userNameRef`: Reference to the current user's name in the database.

## onCreate()

This method is called when the activity is first created. It initializes the activity layout, Firebase references, and sets up click listeners for the start order button, track order button, and cart button. It also calls the `displayName()` method to display the user's name.

## displayName()

This method retrieves the user's name from the Firebase database and sets the `nameView` TextView with the user's name.

## StartOrder()

This method navigates to the `RestaurantListActivity`, where the user can choose a restaurant to order from.

## TrackOrder()

This method navigates to the `CustomerOrderActivity`, where the user can view and track their existing orders.

## toCart()

This method navigates to the `CartActivity`, where the user can view and manage their shopping cart.
# CustomerOrderActivity.java

`CustomerOrderActivity` is an Android Activity class that shows a map with real-time location updates of the delivery person for the customer's order. It extends `FragmentActivity` and implements `OnMapReadyCallback` for the Google Maps integration.

## Class Members

- `ImageButton homeButton, cartButton`: Buttons to navigate to the home screen and the shopping cart screen.
- `GoogleMap mMap`: Google Map instance to display the map.
- `TextView orderID, orderStatus`: TextViews to display the order ID and order status.
- `HashMap<String, Marker> mMarkers`: A HashMap to store the markers on the map.
- `String currentUserID`: The current user's ID.
- `DeliveryOrder currentOrder`: The current order object with delivery details.

## onCreate()

This method is called when the activity is first created. It initializes the activity layout, Google Map, Firebase references, and sets up click listeners for the home button and cart button. It also retrieves the current order and subscribes to real-time updates.

## onMapReady()

This method is called when the map is ready to be used. It initializes the `mMap` instance and sets the maximum zoom level preference.

## toCart()

This method navigates to the `CartActivity`, where the user can view and manage their shopping cart.

## goHome()

This method navigates to the `CustomerActivity`, which is the main screen for customers in the app.

## subscribeToUpdates()

This method sets the order ID and order status TextViews, and subscribes to real-time updates for the delivery person's location if the order is not yet delivered.

## setMarker()

This method updates the marker's position for the delivery person on the map and animates the camera to focus on the updated location.

# Order.java

`Order` is a Java class representing an order in the app. It stores information about the items ordered, the order ID, user ID, name, timestamp, and the total price of the order.

## Class Members

- `ArrayList<MenuItem> mItemsOrdered`: An ArrayList of `MenuItem` objects representing the items ordered.
- `String mOrderID, mUserID, mName`: Strings for the order ID, user ID, and the name of the customer.
- `long mTimestamp`: A long value representing the timestamp of the order.
- `double mPrice`: A double value representing the total price of the order.

## Constructors

- `Order()`: An empty constructor required for data deserialization.
- `Order(ArrayList<MenuItem> itemsOrdered, String orderID, long timestamp, String userID, String name, double price)`: A constructor that initializes the instance variables with the provided values.

## Setters

- `setPrice(double price)`: Sets the price of the order.
- `setName(String name)`: Sets the name of the customer.
- `setUserID(String userID)`: Sets the user ID.
- `setTimeStamp(long timestamp)`: Sets the timestamp of the order.
- `setOrderID(String orderID)`: Sets the order ID.
- `setItemsOrdered(ArrayList<MenuItem> itemsOrdered)`: Sets the ArrayList of items ordered.

## Getters

- `getTimeStamp()`: Returns the timestamp of the order.
- `getOrderID()`: Returns the order ID.
- `getItemsOrdered()`: Returns the ArrayList of items ordered.
- `getUserID()`: Returns the user ID.
- `getName()`: Returns the name of the customer.
- `getPrice()`: Returns the total price of the order.

# Constants.java

`Constants` is a Java class that contains constants to be used throughout the app, specifically for the location service functionality in the context of delivery users.

## Class Members

- `static final int LOCATION_SERVICE_ID`: An integer constant representing the ID of the location service, with a value of `100`.
- `static final String ACTION_START_LOCATION_SERVICE`: A String constant representing the action to start the location service, with a value of `"startLocationService"`.
- `static final String ACTION_STOP_LOCATION_SERVICE`: A String constant representing the action to stop the location service, with a value of `"stopLocationService"`.

The purpose of this class is to provide a centralized place for these constants so that they can be easily managed and updated, if needed. This class is particularly useful when working with background services or broadcasts, where the action strings and service IDs need to be consistent across different parts of the app.
This `DeliveryActivity` class represents the main activity for delivery users in the Nosh app. The purpose of this activity is to display a list of orders available for delivery, allow the delivery user to select an order, and accept it. The activity also provides a button to view the current order, if there is one in progress.

## Main Components

1. **acceptOrderButton**: A button that triggers the `acceptOrder()` method to accept a selected order.
2. **currentOrderButton**: A button that triggers the `goToOrder()` method to navigate to the `DeliveryPickupActivity` for the current order.
3. **orders**: An ArrayList of `DeliveryOrder` objects representing the orders displayed in the RecyclerView.
4. **rvOrderQueue**: A RecyclerView to display the list of orders available for delivery.
5. **listOfCheckBoxes**: A Map containing `DeliveryOrder` objects as keys and their corresponding CheckBoxes as values.
6. **options**: FirebaseRecyclerOptions for configuring the FirebaseRecyclerAdapter.
7. **adapter**: A FirebaseRecyclerAdapter to display the list of orders in the RecyclerView.
8. **currentUserID**: A string representing the ID of the current user.
9. **queueRef**: A DatabaseReference to the "queue" node in the Firebase Realtime Database.
10. **orderInProgress**: A boolean variable that indicates if there is an order in progress.

## Methods

- `onCreate()`: Initializes the activity, setting up the UI elements and initializing the Firebase components.
- `onStart()`: Starts listening for changes in the Firebase Realtime Database and updates the adapter.
- `onStop()`: Stops listening for changes in the Firebase Realtime Database.
- `acceptOrder()`: Accepts an order selected by the delivery user and moves it from the queue to the standby/deliveryStatus nodes in the Firebase Realtime Database.
- `isAnOrderInProgress()`: Checks if the delivery user already has an order in progress by checking the "standby" and "delivering" nodes in the Firebase Realtime Database.
- `goToOrder()`: Navigates to the `DeliveryPickupActivity` for the current order.
- `isOnlyOneOrderChecked()`: Checks if only one order is selected in the RecyclerView.
- `OrderHolder`: An inner class representing the ViewHolder for the RecyclerView items.

When a delivery user selects an order and clicks the "Accept Order" button, the app assigns the order to the user, removes it from the queue, and moves it to the standby/deliveryStatus nodes in the Firebase Realtime Database. If a delivery user already has an order in progress, they cannot accept another order until the current order is completed. The app also provides a button to view the current order and navigate to the `DeliveryPickupActivity`.
The `DeliveryOrder` class extends the `Order` class and adds additional properties specifically related to the delivery process. This class is used to store and manage information about orders that are assigned to delivery users.

## Additional Properties

1. **mLat**: A double value representing the latitude of the delivery destination.
2. **mLon**: A double value representing the longitude of the delivery destination.
3. **mIsEnRoute**: A boolean value indicating if the delivery user is en route to the delivery destination.
4. **mOrderDelivered**: A boolean value indicating if the order has been delivered.
5. **mDeliveryUserID**: A string representing the ID of the delivery user assigned to the order.

## Constructors

- The default constructor `DeliveryOrder()` initializes an empty instance of the class.
- The second constructor `DeliveryOrder(ArrayList<MenuItem> itemsOrdered, String orderID, long timestamp, String userID, String name, double price, double lat, double lon, boolean isEnRoute, String deliveryUserID, boolean orderDelivered)` initializes an instance of the class with specified values for all properties.
- The third constructor `DeliveryOrder(Order order, double lat, double lon, boolean isEnRoute, String deliveryUserID, boolean delivered)` initializes an instance of the class by taking an existing `Order` object and additional delivery-related properties as arguments.

## Getters and Setters

The class provides getters and setters for all additional properties:

- `getLat()`, `setLat(double mLat)`: Get and set the latitude of the delivery destination.
- `getLon()`, `setLon(double mLon)`: Get and set the longitude of the delivery destination.
- `isIsEnRoute()`, `setIsEnRoute(boolean mIsEnRoute)`: Get and set the en route status of the delivery user.
- `getDeliveryUserID()`, `setDeliveryUserID(String mDeliveryUserID)`: Get and set the ID of the delivery user assigned to the order.
- `isOrderDelivered()`, `setOrderDelivered(boolean orderDelivered)`: Get and set the delivery status of the order.
The `DeliveryPickupActivity` class is an Android activity responsible for handling the delivery process for a delivery user, such as marking an order as picked up, marking it as delivered, and starting/stopping the location service.

## Member Variables

- **homeButton**: An `ImageButton` for navigating back to the home screen.
- **markPickedUp**: A `Button` for marking an order as picked up.
- **markDelivered**: A `Button` for marking an order as delivered.
- **nameView**: A `TextView` for displaying the name of the customer.
- **orderView**: A `TextView` for displaying the status of the order.
- **currentOrder**: A `DeliveryOrder` object representing the current order.
- **currentOrderRef**: A `DatabaseReference` pointing to the current order in the Firebase database.
- **orderRef**: A `DatabaseReference` pointing to the standby orders in the Firebase database.
- **ve**: A `ValueEventListener` for monitoring changes in the Firebase database.
- **deliveringRef**: A `DatabaseReference` pointing to the orders being delivered in the Firebase database.
- **currentUserID**: A `String` representing the ID of the current delivery user.
- **deliveryStatusRef**: A `DatabaseReference` pointing to the delivery status of the current order in the Firebase database.

## Methods

### onCreate(@Nullable Bundle savedInstanceState)

The `onCreate` method initializes the activity, sets up the layout, and defines the behavior of the buttons.

### private void goHome()

The `goHome` method navigates back to the home screen (`DeliveryActivity`) and clears the activity stack.

### private void pickedUp()

The `pickedUp` method marks the current order as picked up, updates the Firebase database, requests location permissions if needed, and starts the location service.

### private void delivered()

The `delivered` method marks the current order as delivered, updates the Firebase database, stops the location service, and navigates back to the home screen.

### private boolean isLocationServiceRunning()

The `isLocationServiceRunning` method checks if the location service is running in the foreground.

### private void startLocationService()

The `startLocationService` method starts the `LocationService` if it's not already running.

### private void stopLocationService()

The `stopLocationService` method stops the `LocationService`.

### onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)

The `onRequestPermissionsResult` method is called when the user grants or denies the requested permissions. If the location permission is granted, the location service is started.
The `LocationService` class is an Android service that tracks the location of a delivery user and updates the location data in Firebase in real-time. This service runs in the background and provides location updates to the `delivering` node in the Firebase database.

## Member Variables

- **lc**: A `LocationCallback` that listens for location updates and updates the Firebase database with the delivery user's latitude and longitude.
- **onBind(Intent intent)**: A required method for a Service class, but it returns null in this case as the service is not bound.

## Methods

### private void startLocationService()

The `startLocationService` method creates a `LocationRequest` object with the desired parameters, such as the location update interval and priority. It then checks for the necessary location permissions and requests location updates from the `FusedLocationProviderClient`.

### private void stopLocationService()

The `stopLocationService` method removes location updates from the `FusedLocationProviderClient`, stops the service, and removes it from the foreground.

### public int onStartCommand(Intent intent, int flags, int startId)

The `onStartCommand` method is called when the service is started. It checks if the provided `Intent` contains a valid action and starts or stops the location service accordingly.

This class allows the application to provide real-time location updates to Firebase, enabling efficient tracking of the delivery user's location while delivering orders.
The `LoginActivity` class is an Android activity that handles the user authentication process, allowing users to log in to the app with their email and password. The class checks if the user is a delivery employee or a customer and directs them to the appropriate activity after successful authentication.

## Member Variables

- **emailText**: A `TextInputLayout` containing the user's email.
- **passwordText**: A `TextInputLayout` containing the user's password.
- **loginButton**: A `Button` for triggering the login process.
- **registerButton**: A `Button` for navigating to the registration activity.
- **mAuth**: A `FirebaseAuth` object for authenticating users with Firebase.
- **userTypeRef**: A `DatabaseReference` pointing to the `isEmployee` field in the Firebase database for the current user.
- **isEmployee**: A `String` indicating whether the user is an employee or not.

## Methods

### private void register()

This method starts the `RegisterActivity` and finishes the current activity.

### private void login()

This method retrieves the user's email and password from the input fields and validates them. If the email and password are valid, it attempts to sign in using the Firebase `signInWithEmailAndPassword` method. 

If the authentication is successful, it retrieves the `isEmployee` value from the Firebase database. If the user is an employee, the method starts the `DeliveryActivity`, otherwise, it starts the `CustomerActivity`. The method also displays appropriate error messages in case of authentication failure.

### onCreate()

This method initializes the `LoginActivity` and sets the click listeners for the `loginButton` and `registerButton`. The `loginButton` listener calls the `login()` method, while the `registerButton` listener calls the `register()` method. Additionally, it initializes the `FirebaseAuth` instance.

The `LoginActivity` class handles user authentication and directs users to the appropriate activities based on their roles, ensuring that the app provides a personalized experience for customers and delivery employees.
The `LoginActivity` class is an Android activity that handles the user authentication process, allowing users to log in to the app with their email and password. The class checks if the user is a delivery employee or a customer and directs them to the appropriate activity after successful authentication.

## Member Variables

- **emailText**: A `TextInputLayout` containing the user's email.
- **passwordText**: A `TextInputLayout` containing the user's password.
- **loginButton**: A `Button` for triggering the login process.
- **registerButton**: A `Button` for navigating to the registration activity.
- **mAuth**: A `FirebaseAuth` object for authenticating users with Firebase.
- **userTypeRef**: A `DatabaseReference` pointing to the `isEmployee` field in the Firebase database for the current user.
- **isEmployee**: A `String` indicating whether the user is an employee or not.

## Methods

### private void register()

This method starts the `RegisterActivity` and finishes the current activity.

### private void login()

This method retrieves the user's email and password from the input fields and validates them. If the email and password are valid, it attempts to sign in using the Firebase `signInWithEmailAndPassword` method. 

If the authentication is successful, it retrieves the `isEmployee` value from the Firebase database. If the user is an employee, the method starts the `DeliveryActivity`, otherwise, it starts the `CustomerActivity`. The method also displays appropriate error messages in case of authentication failure.

### onCreate()

This method initializes the `LoginActivity` and sets the click listeners for the `loginButton` and `registerButton`. The `loginButton` listener calls the `login()` method, while the `registerButton` listener calls the `register()` method. Additionally, it initializes the `FirebaseAuth` instance.

The `LoginActivity` class handles user authentication and directs users to the appropriate activities based on their roles, ensuring that the app provides a personalized experience for customers and delivery employees.
