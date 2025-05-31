<?php
// File: goal.php
require 'db_config.php';

header('Content-Type: application/json');

$method = $_SERVER['REQUEST_METHOD'];

if ($method === 'POST') {
    // Save or update goal
    $input = json_decode(file_get_contents('php://input'), true);
    $userId = intval($input['userId'] ?? 0);
    $walking = intval($input['walking'] ?? 0);
    $running = intval($input['running'] ?? 0);
    $cycling = intval($input['cycling'] ?? 0);
    $swimming = intval($input['swimming'] ?? 0);
    if (!$userId) {
        echo json_encode(["success" => false, "message" => "User ID is required."]);
        exit;
    }
    try {
        // Upsert goal for the month (assume one goal per user per month)
        $month = date('Y-m');
        $stmt = $pdo->prepare("SELECT id FROM goals WHERE user_id = :userId AND month = :month");
        $stmt->execute([':userId' => $userId, ':month' => $month]);
        $goal = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($goal) {
            // Update
            $update = $pdo->prepare("UPDATE goals SET walking = :walking, running = :running, cycling = :cycling, swimming = :swimming WHERE id = :id");
            $update->execute([
                ':walking' => $walking,
                ':running' => $running,
                ':cycling' => $cycling,
                ':swimming' => $swimming,
                ':id' => $goal['id']
            ]);
            echo json_encode(["success" => true, "message" => "Goal updated."]);
        } else {
            // Insert
            $insert = $pdo->prepare("INSERT INTO goals (user_id, month, walking, running, cycling, swimming) VALUES (:userId, :month, :walking, :running, :cycling, :swimming)");
            $insert->execute([
                ':userId' => $userId,
                ':month' => $month,
                ':walking' => $walking,
                ':running' => $running,
                ':cycling' => $cycling,
                ':swimming' => $swimming
            ]);
            echo json_encode(["success" => true, "message" => "Goal set."]);
        }
    } catch (PDOException $e) {
        echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
    }
} else if ($method === 'GET') {
    // Get goal for user for current month
    $userId = intval($_GET['userId'] ?? 0);
    $month = date('Y-m');
    if (!$userId) {
        echo json_encode(["success" => false, "message" => "User ID is required."]);
        exit;
    }
    $stmt = $pdo->prepare("SELECT walking, running, cycling, swimming FROM goals WHERE user_id = :userId AND month = :month");
    $stmt->execute([':userId' => $userId, ':month' => $month]);
    $goal = $stmt->fetch(PDO::FETCH_ASSOC);
    if ($goal) {
        echo json_encode(["success" => true, "goal" => $goal]);
    } else {
        echo json_encode(["success" => false, "message" => "No goal set for this month."]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Invalid request method."]);
}
?>
