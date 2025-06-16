<?php
// File: getActivities.php
require 'db_config.php';

header('Content-Type: application/json');

$userId = intval($_GET['userId'] ?? 0);
if (!$userId) {
    echo json_encode(["success" => false, "message" => "User ID is required."]);
    exit;
}

try {
    $stmt = $pdo->prepare("SELECT activity_type, step_count, start_time, end_time FROM activities WHERE user_id = :userId ORDER BY activity_type, start_time DESC");
    $stmt->execute([':userId' => $userId]);
    $activities = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $grouped = [];
    foreach ($activities as $activity) {
        $type = $activity['activity_type'];
        if (!isset($grouped[$type])) $grouped[$type] = [];
        $grouped[$type][] = [
            'step_count' => $activity['step_count'],
            'start_time' => $activity['start_time'],
            'end_time' => $activity['end_time']
        ];
    }
    echo json_encode(["success" => true, "activities" => $grouped]);
} catch (PDOException $e) {
    echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
}
?>
