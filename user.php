<?php
$host = 'localhost';
$user = 'root';
$password = '';
$dbname = 'users';


$conn = new mysqli($host, $user, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Set response header
header('Content-Type: application/json');

// Utility: Get POST value safely
function get($key) {
    return isset($_POST[$key]) ? $_POST[$key] : null;
}

$action = $_GET['action'] ?? 'read_all';

switch ($action) {
    case 'create':
        $name = get('name');
        $email = get('email');
        $password = get('password');

        $stmt = $conn->prepare("INSERT INTO Accounts (name, email, password) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $name, $email, $password);
        if ($stmt->execute()) {
            echo json_encode("User created successfully");
        } else {
            echo json_encode("Error: " . $conn->error);
        }
        $stmt->close();
        break;

    case 'read_all':
        $result = $conn->query("SELECT * FROM Accounts");
        $users = [];

        while ($row = $result->fetch_assoc()) {
            $users[] = $row;
        }

        echo json_encode($users);
        break;

    case 'update':
        $id = get('id');
        $name = get('name');
        $email = get('email');
        $password = get('password');

        $stmt = $conn->prepare("UPDATE Accounts SET name=?, email=?, password=? WHERE id=?");
        $stmt->bind_param("sssi", $name, $email, $password, $id);
        if ($stmt->execute()) {
            echo json_encode("User updated successfully");
        } else {
            echo json_encode("Error: " . $conn->error);
        }
        $stmt->close();
        break;

    case 'delete':
        $id = get('id');

        $stmt = $conn->prepare("DELETE FROM Accounts WHERE id=?");
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            echo json_encode("User deleted successfully");
        } else {
            echo json_encode("Error: " . $conn->error);
        }
        $stmt->close();
        break;

    default:
        echo json_encode("Invalid action");
        break;
}

$conn->close();
?>
