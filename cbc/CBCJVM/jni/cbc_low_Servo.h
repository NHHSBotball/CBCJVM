/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class cbc_low_Servo */

#ifndef _Included_cbc_low_Servo
#define _Included_cbc_low_Servo
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     cbc_low_Servo
 * Method:    enable_servos
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cbc_low_Servo_enable_1servos
  (JNIEnv *, jobject);

/*
 * Class:     cbc_low_Servo
 * Method:    disable_servos
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cbc_low_Servo_disable_1servos
  (JNIEnv *, jobject);

/*
 * Class:     cbc_low_Servo
 * Method:    set_servo_position
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_cbc_low_Servo_set_1servo_1position
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     cbc_low_Servo
 * Method:    get_servo_position
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_cbc_low_Servo_get_1servo_1position
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
