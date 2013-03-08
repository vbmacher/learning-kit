/* 
 * File:   synchronization.h
 * Author: vbmacher
 *
 * Created on Nedeľa, 2013, február 24, 14:37
 */

#ifndef SYNCHRONIZATION_H
#define	SYNCHRONIZATION_H

#include <boost/thread/mutex.hpp>
#include <boost/thread/locks.hpp>
#include <SDL_stdinc.h>

namespace github {
    namespace pong {
        
        template<typename T>
        class Locked {
            volatile T var;
            mutable boost::mutex varMutex;
            
        public:
            Locked(T variable) : var(variable) {}
            
            Locked &operator=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                var = value;
                return *this;
            }
            
            Locked &operator+=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                var += value;
                return *this;
            }
            
            T operator+(const T& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var + value;
            }
            
            T operator-(const T& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var - value;
            }

            T operator*(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var * value;
            }
            
            bool operator==(const T& value) const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var == value;
            }
            
            bool operator!=(const T& value) const {
                return !(*this == value);
            }
            
            bool operator<=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var <= value;
            }
            
            bool operator>=(const T& value) {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var >= value;
            }
            
            operator bool() const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var;
            }

            operator double() const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var;
            }
            
            operator Uint16() const {
                boost::lock_guard<boost::mutex> lock(varMutex);
                return var;
            }
        };

    }
}

#endif	/* SYNCHRONIZATION_H */

