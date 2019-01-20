import RPi.GPIO as GPIO

def distance():

	GPIO.setmode(GPIO.BCM)

	OUTPUT = 23 
	INPUT = 24

	print "Distance Measurement In Progress"

	GPIO.setup(OUTPUT,GPIO.OUT)
	GPIO.setup(INPUT,GPIO.IN)

	GPIO.output(OUTPUT, False)
	print "Waiting For Sensor To Settle"
	time.sleep(2)

	GPIO.output(OUTPUT, True)
	time.sleep(0.00001)
	GPIO.output(OUTPUT, False)

	while GPIO.input(INPUT)==0:
  		pulse_start = time.time()

	while GPIO.input(INPUT)==1:
  		pulse_end = time.time()

	pulse_duration = pulse_end - pulse_start

	distance = pulse_duration * 17150

	distance = round(distance, 2)

	print "Distance:",distance,"cm"
	GPIO.cleanup()
	return distance

##


