import com.jweir.socialgraph.domain.Connection
import com.jweir.socialgraph.domain.User

def userRepository = repositories['userRepository']
def connectionRepository = repositories['connectionRepository']

userRepository.deleteAll()
connectionRepository.deleteAll()

userRepository.save([
    new User(name: 'Jim'),
    new User(name: 'Andrew'),
    new User(name: 'Josh'),
    new User(name: 'Mike'),
    new User(name: 'Sean')
])

def jim = userRepository.findByName('Jim')
connectionRepository.save([
    new Connection(jim, userRepository.findByName('Andrew')),
    new Connection(jim, userRepository.findByName('Josh')),
    new Connection(jim, userRepository.findByName('Mike'))
])

def andrew = userRepository.findByName('Andrew')
connectionRepository.save([
    new Connection(andrew, userRepository.findByName('Josh'))
])

def josh = userRepository.findByName('Josh')
connectionRepository.save([
    new Connection(josh, userRepository.findByName('Sean'))
])
