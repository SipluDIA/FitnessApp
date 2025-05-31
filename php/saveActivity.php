<?php
// File: saveActivity.php
require 'db_config.php';

header('Content-Type: application/json');

// Get POST data
$input = json_decode(file_get_contents('php://input'), true);
if (!$input) {
    // Fallback to form data if not JSON
    $input = $_POST;
}

$userId = intval($input['user_id'] ?? 0);
$activityType = trim($input['activity_type'] ?? '');
$stepCount = intval($input['step_count'] ?? 0);
$startTime = trim($input['start_time'] ?? '');
$endTime = trim($input['end_time'] ?? '');

if (!$userId || !$activityType || !$startTime || !$endTime) {
    echo json_encode(["success" => false, "message" => "Missing required fields."]);
    exit;
}

try {
    $stmt = $pdo->prepare("INSERT INTO activities (user_id, activity_type, step_count, start_time, end_time) VALUES (:user_id, :activity_type, :step_count, :start_time, :end_time)");
    $stmt->execute([
        ':user_id' => $userId,
        ':activity_type' => $activityType,
        ':step_count' => $stepCount,
        ':start_time' => $startTime,
        ':end_time' => $endTime
    ]);
    echo json_encode(["success" => true, "message" => "Activity saved successfully."]);
} catch (PDOException $e) {
    echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
}
?>
