README.md — YAC Algorithm Project

🏈 YAC Algorithm — Dynamic Path-Planning for Maximizing Yards After Catch

The YAC Algorithm Project is a Java-based simulation and optimization system designed to compute the most efficient football receiver routes after catching the ball.
Using a D* Lite dynamic path-planning algorithm, the system re-evaluates optimal running paths in real time as defenders move, react, and close space—mirroring the real, adaptive decision-making required on the field.

This tool is built for coaching insights, play-design experimentation, and performance analytics.

⸻

Features

✔ Dynamic D* Lite Path-Planning
	•	Computes optimal receiver paths on a 2D football field.
	•	Re-plans automatically as defenders change position.
	•	Models the constant motion and unpredictability of real defensive pressure.

✔ 2,000+ Football Play Simulations
	•	Large simulation batch to evaluate route decisions across:
	•	Different defensive formations
	•	Varying player speeds
	•	Different catch points
	•	Open-field vs congested situations

✔ Interactive Visualizations
	•	Real-time playback of simulated plays.
	•	Display of:
	•	Receiver path
	•	Defender pursuit paths
	•	High-value running lanes
	•	Final route chosen by D* Lite

✔ Yards After Catch (YAC) Optimization
	•	Primary objective: maximize total yardage gained post-catch.
	•	Cost fields represent defender proximity and pursuit angles.
	•	Algorithm favors open lanes, safe angles, and maximal forward progress.

✔ Coaching & Analytics Utility
	•	Helps explore:
	•	Optimal receiver cut angles
	•	Defensive weaknesses
	•	Formation-specific advantages
	•	Effects of spacing and timing

⸻

How the Algorithm Works

1. Field Modeling

The football field is discretized into a 2D grid with:
	•	Free spaces
	•	Dynamic obstacles (defenders)
	•	Cost-weighted zones based on defender distance

2. Catch Point Initialization

The simulation begins the moment a receiver catches the ball.
The D* Lite planner initializes at the catch point.

3. Dynamic Replanning

Each simulation frame:
	1.	Defenders move according to pursuit behavior.
	2.	The algorithm updates the cost map.
	3.	D* Lite recomputes the least-cost path that maximizes open-field yardage.
	4.	The receiver path updates accordingly.

4. Metrics Collected
	•	Total YAC
	•	Time to reach end zone / defender contact
	•	Route smoothness and angle changes
	•	Lane-selection patterns

<img width="702" height="839" alt="Screenshot 2025-11-25 at 6 31 27 AM" src="https://github.com/user-attachments/assets/09cb563a-fbae-495a-84c8-5a3d8c79ef25" />

